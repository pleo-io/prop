package io.pleo.prop.objects

import com.google.inject.Inject
import com.google.inject.name.Named
import io.pleo.prop.core.Prop

@Suppress("UNUSED_PARAMETER")
class InvalidJSON
@Inject
constructor(
    @Named("io.pleo.prop5") objectProp: Prop<InjectedObject>,
)
