package io.pleo.prop.objects

import com.google.inject.Inject
import com.google.inject.name.Named
import io.pleo.prop.core.Default
import io.pleo.prop.core.Prop

class DefaultValue @Inject constructor(
    @Default(DEFAULT_VALUE)
    @Named("io.pleo.undefined.property")
    val usesDefaultValue: Prop<String>
) {
    companion object {
        const val DEFAULT_VALUE = "This is the default value!"
    }
}
