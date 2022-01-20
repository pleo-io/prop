package io.pleo.prop.jackson

import io.pleo.prop.core.PropException

class FailedToParsePropValueException(cause: Throwable) :
    PropException("Failed to parse property value.", cause)
