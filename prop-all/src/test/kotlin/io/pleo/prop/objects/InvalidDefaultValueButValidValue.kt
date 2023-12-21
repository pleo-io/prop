package io.pleo.prop.objects

import com.google.inject.Inject
import com.google.inject.name.Named
import io.pleo.prop.core.Default
import io.pleo.prop.core.Prop

@Suppress("unused")
class InvalidDefaultValueButValidValue @Inject constructor(
    @Default(DEFAULT_VALUE)
    @Named("io.pleo.test.prop6")
    val usesDefaultValue: Prop<Double>
) {

    companion object {
        const val DEFAULT_VALUE = "This is not a double!"
    }
}
