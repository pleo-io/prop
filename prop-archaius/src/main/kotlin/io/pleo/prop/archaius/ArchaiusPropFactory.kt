package io.pleo.prop.archaius

import io.pleo.prop.core.Parser
import io.pleo.prop.core.Prop
import io.pleo.prop.core.internal.PropFactory

class ArchaiusPropFactory : PropFactory {
    override fun <T> createProp(
        propName: String,
        parse: Parser<T>,
        defaultValue: T?,
    ): Prop<T> =
        ArchaiusProp(
            ParsingProperty(propName, parse, defaultValue),
        )
}
