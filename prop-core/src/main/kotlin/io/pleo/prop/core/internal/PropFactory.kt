package io.pleo.prop.core.internal

import io.pleo.prop.core.Parser
import io.pleo.prop.core.Prop

@FunctionalInterface
interface PropFactory {
    fun <T> createProp(
        propName: String,
        parse: Parser<T>,
        defaultValue: T?
    ): Prop<T>
}
