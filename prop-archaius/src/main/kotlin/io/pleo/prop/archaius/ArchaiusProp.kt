package io.pleo.prop.archaius

import com.netflix.config.Property
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

    override fun getName(): String {
        return archaiusProperty.name
    }

    override fun getChangedTimestamp(): Instant {
        return Instant.ofEpochMilli(archaiusProperty.changedTimestamp)
    }

    override fun addCallback(callback: Runnable) {
        archaiusProperty.addCallback(callback)
    }

    override fun removeAllCallbacks() {
        archaiusProperty.removeAllCallbacks()
    }

    override fun toString(): String {
        return archaiusProperty.toString()
    }
}
