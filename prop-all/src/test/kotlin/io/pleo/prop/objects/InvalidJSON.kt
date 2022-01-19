package io.pleo.prop.objects

import io.pleo.prop.core.Prop
import javax.inject.Inject
import javax.inject.Named

@Suppress("UNUSED_PARAMETER")
class InvalidJSON @Inject constructor(@Named("io.pleo.prop5") objectProp: Prop<InjectedObject>)
