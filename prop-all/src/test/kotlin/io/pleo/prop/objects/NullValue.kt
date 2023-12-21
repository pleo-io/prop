package io.pleo.prop.objects

import com.google.inject.Inject
import com.google.inject.name.Named
import io.pleo.prop.core.Prop

@Suppress("unused_parameter")
class NullValue @Inject constructor(@Named("io.pleo.undefined.property") thisThrows: Prop<String>)
