package io.pleo.prop.core.internal

import io.pleo.prop.core.Callback
import io.pleo.prop.core.Prop
import io.pleo.prop.core.Supplier
import java.time.Instant

class LocalProp<T>(
    private val supplier: Supplier<T>,
) : Prop<T> {
    private val created: Instant = Instant.now()

    constructor(value: T) : this({ value })

    override fun invoke() = supplier()

    override fun get() = invoke()

    override val name get() = toString()
    override val changedTimestamp get() = created

    /** Static properties can't change. No callbacks.*/
    override fun addCallback(callback: Callback<T>) = Unit

    override fun removeAllCallbacks() = Unit

    override fun toString() = "LocalProp=${get()}"
}
