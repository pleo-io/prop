package io.pleo.prop.objects

import com.google.inject.Inject
import com.google.inject.name.Named
import io.pleo.prop.core.Prop

class UsesTwiceSameProp @Inject constructor(
    @Named("io.pleo.test.prop3") val stringProp1: Prop<String>,
    @Named("io.pleo.test.prop3") val stringProp2: Prop<String>
)
