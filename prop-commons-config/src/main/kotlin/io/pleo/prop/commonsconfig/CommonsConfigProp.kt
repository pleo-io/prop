package io.pleo.prop.commonsconfig

import io.pleo.prop.core.Callback
import io.pleo.prop.core.Prop
import java.time.Instant

class CommonsConfigProp<T>(
    private val parsingProperty: ParsingProperty<T>
) : Prop<T> {
    override val name: String
        get() = parsingProperty.name

    override fun get(): T = parsingProperty.getValue()
    override val changedTimestamp: Instant
        get() = parsingProperty.changedTimestamp

    override fun addCallback(callback: Callback<T>) = parsingProperty.addCallback(callback)
    override fun removeAllCallbacks() = parsingProperty.removeAllCallbacks()
    override fun toString(): String = "$name=${get()}"
}
