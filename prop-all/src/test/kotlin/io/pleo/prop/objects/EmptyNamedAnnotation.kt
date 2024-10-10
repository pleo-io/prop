package io.pleo.prop.objects

import com.google.inject.Inject
import io.pleo.prop.core.Prop
import jakarta.inject.Named

@Suppress("UNUSED_PARAMETER")
class EmptyNamedAnnotation
@Inject
constructor(
    @Named myProp: Prop<String>,
)
