package io.pleo.prop.guice.internal

import io.pleo.prop.core.Prop

class PropResult {
    var prop: Prop<*>? = null
        private set

    var error: Throwable? = null
        private set

    constructor(prop: Prop<*>?) {
        this.prop = prop
    }

    constructor(error: Throwable?) {
        this.error = error
    }

    fun isError() = error != null
}
