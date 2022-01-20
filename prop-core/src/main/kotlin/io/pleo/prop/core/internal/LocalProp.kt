package io.pleo.prop.core.internal

import io.pleo.prop.core.Prop
import java.time.Instant
import java.util.function.Supplier

class LocalProp<T>(
    private val supplier: Supplier<T>,
) : Prop<T> {
    private val created: Instant = Instant.now()

    constructor(value: T) : this(Supplier<T> { value })

    override fun invoke() = supplier.get()
    override fun get() = invoke()

    override val name get() = toString()
    override val changedTimestamp get() = created

    /** Static properties can't change. No callbacks.*/
    override fun addCallback(callback: Runnable) {}
    override fun removeAllCallbacks() {}

    override fun toString() = "LocalProp=${get()}"
}
