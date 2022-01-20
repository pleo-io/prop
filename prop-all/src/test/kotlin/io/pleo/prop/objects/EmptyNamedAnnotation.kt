package io.pleo.prop.objects

import io.pleo.prop.core.Prop
import javax.inject.Inject
import javax.inject.Named

@Suppress("UNUSED_PARAMETER")
class EmptyNamedAnnotation @Inject constructor(@Named myProp: Prop<String>)
