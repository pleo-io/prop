package io.pleo.prop.guice.internal

import com.google.inject.Binding
import com.google.inject.Key
import com.google.inject.TypeLiteral
import com.google.inject.spi.DefaultElementVisitor
import com.google.inject.spi.Element
import com.google.inject.spi.InjectionPoint
import com.google.inject.spi.PrivateElements
import com.google.inject.spi.ProviderLookup
import io.pleo.prop.core.Default
import io.pleo.prop.core.Prop
import io.pleo.prop.core.internal.ParserFactory
import io.pleo.prop.core.internal.PropFactory
import java.lang.reflect.Executable
import java.lang.reflect.Parameter
import java.lang.reflect.ParameterizedType
import java.util.Optional.ofNullable
import java.util.function.Predicate
import javax.inject.Named

/**
 * Goes over every binding and produces a Map associating Guice binding keys to
 * Pleo Prop instances.
 */
class PropMappingVisitor(
    filter: Predicate<TypeLiteral<*>>,
    private val propFactory: PropFactory,
    private val parserFactory: ParserFactory,
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

            injectionPoint.dependencies.forEach { dependency ->
                @Suppress("unchecked_cast")
                val key = dependency.key
                    .takeIf {
                        it.typeLiteral.rawType == Prop::class.java &&
                            it.typeLiteral.type is ParameterizedType
                    } as Key<Prop<*>>?
                    ?: return@forEach

                val value =
                    try {
                        val parameter = parameters[dependency.parameterIndex]
                        PropResult(parameterToProp(parameter, key))
                    } catch (ex: RuntimeException) {
                        PropResult(ex)
                    }

                set(key, value)
            }
        }

    private fun parameterToProp(parameter: Parameter, key: Key<*>): Prop<*> {
        val propertyName: String = getNamedAnnotationValue(parameter.annotations.toList(), key)

        val parameterizedType = parameter.parameterizedType as ParameterizedType
        val type = parameterizedType.actualTypeArguments.first()
        val parser = parserFactory.createParserForType(type)
        try {
            val annotation = parameter.getAnnotation(Default::class.java)
            println("ANNOTATION: $annotation")
            val defaultValue = ofNullable<Default>(annotation)
                .map(Default::value)
                .map(parser::apply)
                .orElse(null)
            println("DEF: $defaultValue")

            return propFactory.createProp(propertyName, parser, defaultValue)
        } catch (ex: RuntimeException) {
            throw FailedToCreatePropException(propertyName, ex)
        }
    }

    private fun getNamedAnnotationValue(annotations: List<Annotation>, key: Key<*>): String =
        annotations.mapNotNull(::annotationValueIfNamed).lastOrNull().also {
            println("?? $it")
        }
            ?: throw RequiredNamedAnnotationException(key)

    private fun annotationValueIfNamed(annotation: Annotation): String? =
        when (annotation) {
            is Named -> annotation.value
            is com.google.inject.name.Named -> annotation.value
            else -> null
        }?.ifEmpty { null }.also {
            println(">> $it")
        }
}
