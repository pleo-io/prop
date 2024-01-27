package io.pleo.prop.objects

import com.google.inject.Inject
import io.pleo.prop.core.Prop
import com.google.inject.name.Named as GoogleNamed
import jakarta.inject.Named as JakartaNamed

class BothNamedAnnotations @Inject constructor(
    @JakartaNamed("io.pleo.test.prop3") val stringProp1: Prop<String>,
    @GoogleNamed("io.pleo.test.prop4") val stringProp2: Prop<String>
)
