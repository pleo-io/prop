package io.pleo.prop.core

import io.pleo.prop.core.internal.LocalProp

@Suppress("unused")
object Props {
    /**
     * Creates a static Prop that will always have the provided value.
     *
     * @param value The static value of the Prop
     * @param <T>   The type of the Prop
     * @return A Prop instance that will always return the provided value.
     */
    @JvmStatic
    fun <T> of(value: T) = LocalProp(value)

    /**
     * Creates a dynamic Prop that will evaluate the provided supplier every time it is read.
     *
     * @param supplier The Supplier that will be called when the Prop's value is read
     * @param <T>      The type of the Prop
     * @return A Prop instance that will always return the value returned by the provided supplier.
     */
    @JvmStatic
    fun <T> of(supplier: Supplier<T>) = LocalProp(supplier)
}
