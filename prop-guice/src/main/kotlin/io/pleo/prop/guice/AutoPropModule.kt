package io.pleo.prop.guice

import com.google.inject.Binder
import com.google.inject.Module
import com.google.inject.spi.Elements
import io.pleo.prop.core.internal.ParserFactory
import io.pleo.prop.core.internal.PropFactory
import io.pleo.prop.guice.internal.PropMappingVisitor

class AutoPropModule(
    private val packagePrefix: String,
    private val modulesToScan: Iterable<Module>,
    private val propFactory: PropFactory,
    private val parserFactory: ParserFactory,
) : Module {
    override fun configure(binder: Binder) {
        PropMappingVisitor(
            { it.rawType.getPackage().name.startsWith(packagePrefix) },
            propFactory,
            parserFactory,
        )
            .visit(Elements.getElements(modulesToScan))
            .forEach { (key, value) ->
                value.fold(
                    { binder.bind(key).toInstance(it) },
                    { binder.addError(it) },
                )
            }
    }
}
