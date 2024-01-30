package io.pleo.prop.objects

import com.google.inject.Inject
import com.google.inject.name.Named
import io.pleo.prop.core.Prop

class SamePropertyAsComplexObjects
@Inject
constructor(
    @Named("io.pleo.test.prop1") val myComplexObjectProp: Prop<InjectedObject>,
)
