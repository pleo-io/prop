package io.pleo.prop.archaius

import io.pleo.prop.core.Prop
import io.pleo.prop.core.internal.PropFactory
import java.util.function.Function

class ArchaiusPropFactory : PropFactory {
    override fun <T> createProp(propName: String, parse: Function<String, T>, defaultValue: T): Prop<T> =
        ArchaiusProp(ParsingProperty(propName, parse, defaultValue))
}
