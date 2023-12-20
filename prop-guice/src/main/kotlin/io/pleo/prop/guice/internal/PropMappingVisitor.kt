package io.pleo.prop.guice.internal

import com.google.inject.Binding
import com.google.inject.Key
import com.google.inject.TypeLiteral
import com.google.inject.spi.DefaultElementVisitor
import com.google.inject.spi.Dependency
import com.google.inject.spi.Element
import com.google.inject.spi.InjectionPoint
import com.google.inject.spi.PrivateElements
import com.google.inject.spi.ProviderLookup
import io.pleo.prop.core.Default
import io.pleo.prop.core.Parser
import io.pleo.prop.core.Prop
import io.pleo.prop.core.internal.ParserFactory
import io.pleo.prop.core.internal.PropFactory
import jakarta.inject.Named
import java.lang.reflect.Executable
import java.lang.reflect.Parameter
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.Optional.ofNullable
import java.util.function.Predicate

typealias PropResult = Result<Prop<*>>

/**
 * Goes over every binding and produces a Map associating Guice binding keys to
 * Pleo Prop instances.
 */
class PropMappingVisitor(
    filter: Predicate<TypeLiteral<*>>,
    private val propFactory: PropFactory,
    private val parserFactory: ParserFactory
) : DefaultElementVisitor<Map<Key<Prop<*>>, PropResult>>() {
    private val injectionPointExtractor: InjectionPointExtractor = InjectionPointExtractor(filter)

    fun visit(elements: Iterable<Element>): Map<Key<Prop<*>>, PropResult> {
        val mappings: MutableMap<Key<Prop<*>>, PropResult> = HashMap()
        for (element in elements) {
            val visitResult = element.acceptVisitor(this)
            // acceptVisitor returns null if the visitor is never called
            if (visitResult != null) {
                // We might encounter duplicates (multiple classes using the same property) but they will be overwritten.
                mappings.putAll(visitResult)
            }
        }
        return mappings
    }

    override fun visit(privateElements: PrivateElements): Map<Key<Prop<*>>, PropResult> {
        return visit(privateElements.elements)
    }

    override fun <T> visit(binding: Binding<T>): Map<Key<Prop<*>>, PropResult> {
        return extractProps(binding.acceptTargetVisitor(injectionPointExtractor))
    }

    override fun <T> visit(providerLookup: ProviderLookup<T>): Map<Key<Prop<*>>, PropResult> {
        return extractProps(providerLookup.dependency.injectionPoint)
    }

    private fun extractProps(injectionPoints: Iterable<InjectionPoint>?): Map<Key<Prop<*>>, PropResult> =
        buildMap {
            (injectionPoints ?: emptyList())
                .map(::extractProps)
                .forEach(::putAll)
        }

    private fun extractProps(injectionPoint: InjectionPoint?): Map<Key<Prop<*>>, PropResult> =
        buildMap {
            val parameters = (injectionPoint?.member as? Executable)
                ?.parameters
                ?.toList()
                ?: return@buildMap

            injectionPoint.dependencies
                .filterIsPropDependency()
                .forEach { dependency ->
                    buildAndSet(dependency.key) {
                        parameters[dependency.parameterIndex].toProp(dependency.key)
                    }
                }
        }

    @Suppress("unchecked_cast")
    private fun List<Dependency<*>>.filterIsPropDependency(): List<Dependency<Prop<*>>> =
        filter {
            it.key.typeLiteral.rawType == Prop::class.java &&
                it.key.typeLiteral.type is ParameterizedType
        } as List<Dependency<Prop<*>>>

    private fun <K, V> MutableMap<K, Result<V>>.buildAndSet(key: K, builder: () -> V) =
        set(key, runCatching(builder))

    private fun Parameter.toProp(key: Key<*>): Prop<*> {
        val propertyName: String = getNamedAnnotationValue(annotations.toList(), key)
        val parser: Parser<*> = createParameterParser(this)

        try {
            val annotation = getAnnotation(Default::class.java)
            val defaultValue = ofNullable<Default>(annotation)
                .map(Default::value)
                .map(parser)
                .orElse(null)

            @Suppress("UNCHECKED_CAST")
            return propFactory.createProp(
                propertyName,
                parser as Parser<Any>,
                defaultValue
            )
        } catch (ex: RuntimeException) {
            throw FailedToCreatePropException(propertyName, ex)
        }
    }

    private fun createParameterParser(parameter: Parameter): Parser<*> {
        val parameterizedType = parameter.parameterizedType as ParameterizedType
        val type: Type = parameterizedType.actualTypeArguments.first()
        return parserFactory.createParserForType(type)
    }

    private fun getNamedAnnotationValue(annotations: List<Annotation>, key: Key<*>): String =
        annotations.mapNotNull(::annotationValueIfNamed).lastOrNull()
            ?: throw RequiredNamedAnnotationException(key)

    private fun annotationValueIfNamed(annotation: Annotation): String? =
        when (annotation) {
            is Named -> annotation.value
            is com.google.inject.name.Named -> annotation.value
            else -> null
        }?.ifEmpty { null }
}
