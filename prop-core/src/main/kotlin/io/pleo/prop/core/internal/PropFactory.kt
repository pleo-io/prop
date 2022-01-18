package io.pleo.prop.core.internal

import io.pleo.prop.core.Prop
import java.util.function.Function

@FunctionalInterface
interface PropFactory {
    fun <T> createProp(propName: String, parse: Function<String, T>, defaultValue: T): Prop<T>
}
