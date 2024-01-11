package io.pleo.prop.objects

import io.pleo.prop.core.Prop
import javax.inject.Inject
import com.google.inject.name.Named as GoogleNamed
import javax.inject.Named as JavaxNamed

class BothNamedAnnotations
@Inject
constructor(
    @JavaxNamed("io.pleo.test.prop3") val stringProp1: Prop<String>,
    @GoogleNamed("io.pleo.test.prop4") val stringProp2: Prop<String>
)
