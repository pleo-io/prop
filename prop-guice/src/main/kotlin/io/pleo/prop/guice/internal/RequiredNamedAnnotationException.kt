package io.pleo.prop.guice.internal

import com.google.inject.Key
import io.pleo.prop.core.PropException

class RequiredNamedAnnotationException(key: Key<*>) :
    PropException("Property identified by key '$key' has no @Named annotation.")
