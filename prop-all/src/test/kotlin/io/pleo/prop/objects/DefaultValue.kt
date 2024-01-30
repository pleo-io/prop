package io.pleo.prop.objects

import io.pleo.prop.core.Default
import io.pleo.prop.core.Prop
import javax.inject.Inject
import javax.inject.Named

class DefaultValue
@Inject
constructor(
    @Default(DEFAULT_VALUE)
    @Named("io.pleo.undefined.property")
    val usesDefaultValue: Prop<String>,
) {
    companion object {
        const val DEFAULT_VALUE = "This is the default value!"
    }
}
