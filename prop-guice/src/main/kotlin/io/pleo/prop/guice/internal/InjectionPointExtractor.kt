package io.pleo.prop.guice.internal

import com.google.inject.ConfigurationException
import com.google.inject.Key
import com.google.inject.TypeLiteral
import com.google.inject.assistedinject.AssistedInjectBinding
import com.google.inject.assistedinject.AssistedInjectTargetVisitor
import com.google.inject.spi.DefaultBindingTargetVisitor
import com.google.inject.spi.InjectionPoint
import com.google.inject.spi.LinkedKeyBinding
import com.google.inject.spi.ProviderKeyBinding
import com.google.inject.spi.ProvidesMethodBinding
import com.google.inject.spi.ProvidesMethodTargetVisitor
import com.google.inject.spi.UntargettedBinding
import org.slf4j.LoggerFactory
import java.util.function.Predicate

private val logger = LoggerFactory.getLogger(InjectionPointExtractor::class.java)

class InjectionPointExtractor(private val filter: Predicate<TypeLiteral<*>>) :
    DefaultBindingTargetVisitor<Any?, Iterable<InjectionPoint>>(),
    ProvidesMethodTargetVisitor<Any?, Iterable<InjectionPoint>>,
    AssistedInjectTargetVisitor<Any?, Iterable<InjectionPoint>> {
    override fun visit(untargettedBinding: UntargettedBinding<*>): Iterable<InjectionPoint> =
        listOfNotNull(getInjectionPointForKey(untargettedBinding.key))

    override fun visit(assistedInjectBinding: AssistedInjectBinding<*>): Iterable<InjectionPoint> {
        val injectionPoints: MutableCollection<InjectionPoint> = ArrayList()
        for (assistedMethod in assistedInjectBinding.assistedMethods) {
            try {
                injectionPoints.add(InjectionPoint.forConstructorOf(assistedMethod.implementationType))
            } catch (e: ConfigurationException) {
                logger.info("Skipping assistedMethod type {}: {}", assistedMethod.implementationType, e.message)
            }
        }
        return injectionPoints
    }

    override fun visit(linkedKeyBinding: LinkedKeyBinding<*>): Iterable<InjectionPoint> =
        listOfNotNull(getInjectionPointForKey(linkedKeyBinding.linkedKey))

    override fun visit(providerKeyBinding: ProviderKeyBinding<*>): Iterable<InjectionPoint> =
        listOfNotNull(getInjectionPointForKey(providerKeyBinding.providerKey))

    override fun visit(providesMethodBinding: ProvidesMethodBinding<*>): Iterable<InjectionPoint>? =
        try {
            TypeLiteral.get(providesMethodBinding.enclosingInstance.javaClass)
                .takeIf { filter.test(it) }
                ?.let { type ->
                    listOf(InjectionPoint.forMethod(providesMethodBinding.method, type))
                }
        } catch (e: ConfigurationException) {
            logger.info("Skipping key {}: {}", providesMethodBinding, e.message)
            null
        }

    private fun getInjectionPointForKey(key: Key<*>): InjectionPoint? =
        try {
            key.typeLiteral.takeIf { filter.test(it) }
                ?.let(InjectionPoint::forConstructorOf)
        } catch (e: ConfigurationException) {
            logger.info("Skipping key {}: {}", key, e.message)
            null
        }
}
