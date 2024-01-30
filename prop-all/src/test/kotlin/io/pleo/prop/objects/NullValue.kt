package io.pleo.prop.objects

import io.pleo.prop.core.Prop
import javax.inject.Inject
import javax.inject.Named

@Suppress("unused", "UNUSED_PARAMETER")
class NullValue
@Inject
constructor(
    @Named("io.pleo.undefined.property") thisThrows: Prop<String>,
)
