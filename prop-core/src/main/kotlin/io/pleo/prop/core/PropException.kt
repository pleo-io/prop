package io.pleo.prop.core

open class PropException(
    message: String? = null,
    cause: Throwable? = null,
) : RuntimeException(message, cause)
