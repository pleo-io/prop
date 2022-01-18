package io.pleo.prop.core

import java.time.Instant
import java.util.function.Supplier

/**
 * A Prop is a configured property for an application.
 * It is often dynamic and its value should not be cached.
 *
 *
 * Use invocation (`()`) or `.get()` to get the value of the Prop
 */
interface Prop<T> : Supplier<T> {
    /** Get the name of the property */
    val name: String

    /** Convenience method to implement invocation for Kotlin */
    operator fun invoke(): T

    /** Gets the time when the property was last set/changed. */
    val changedTimestamp: Instant

    /** Add the callback to be triggered when the value of the property is changed */
    fun addCallback(callback: Runnable)

    /** Remove all callbacks registered through the instance of property */
    fun removeAllCallbacks()
}
