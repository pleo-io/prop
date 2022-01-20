package io.pleo.prop.guice.internal

import io.pleo.prop.core.PropException

class FailedToCreatePropException(propName: String, cause: Throwable?) :
    PropException("Failed to create prop '$propName'.", cause)
