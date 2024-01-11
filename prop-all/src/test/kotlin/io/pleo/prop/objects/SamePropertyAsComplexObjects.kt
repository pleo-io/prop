package io.pleo.prop.objects

import io.pleo.prop.core.Prop
import javax.inject.Inject
import javax.inject.Named

class SamePropertyAsComplexObjects
@Inject
constructor(
    @Named("io.pleo.test.prop1") val myComplexObjectProp: Prop<InjectedObject>
)
