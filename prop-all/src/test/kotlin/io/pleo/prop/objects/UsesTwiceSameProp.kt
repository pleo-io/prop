package io.pleo.prop.objects

import io.pleo.prop.core.Prop
import javax.inject.Inject
import javax.inject.Named

class UsesTwiceSameProp @Inject constructor(
    @Named("io.pleo.test.prop3") val stringProp1: Prop<String>,
    @Named("io.pleo.test.prop3") val stringProp2: Prop<String>
)
