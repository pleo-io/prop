package io.pleo.prop.archaius

import com.netflix.config.Property
import io.pleo.prop.core.Callback
import io.pleo.prop.core.Prop
import java.time.Instant

/**
 * Prop that wraps a com.netflix.config.Property
 */
class ArchaiusProp<T>(private val archaiusProperty: Property<T>) : Prop<T> {
    override fun get(): T {
        return archaiusProperty.value
    }

    override fun invoke(): T {
        return get()
    }

    override val name: String
        get() = archaiusProperty.name

    override val changedTimestamp: Instant
        get() = Instant.ofEpochMilli(archaiusProperty.changedTimestamp)

    override fun addCallback(callback: Callback<T>) =
        archaiusProperty.addCallback {
            callback(get())
        }

    override fun removeAllCallbacks() = archaiusProperty.removeAllCallbacks()
    override fun toString() = archaiusProperty.toString()
}
