package io.pleo.prop.objects

import io.pleo.prop.core.Prop
import jakarta.inject.Inject
import jakarta.inject.Named

@Suppress("UNUSED_PARAMETER")
class EmptyNamedAnnotation @Inject constructor(@Named myProp: Prop<String>)
