package io.pleo.prop.core

open class PropException(
    message: String? = null,
    cause: Throwable? = null,
) : RuntimeException(message, cause) {
    constructor(message: String?) : this(message, null)
    constructor(cause: Throwable?) : this(null, cause)
}
