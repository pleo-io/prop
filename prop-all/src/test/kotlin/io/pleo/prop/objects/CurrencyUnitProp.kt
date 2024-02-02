package io.pleo.prop.objects

import com.google.inject.Inject
import com.google.inject.name.Named
import io.pleo.prop.core.Prop
import javax.money.CurrencyUnit

class CurrencyUnitProp
@Inject
constructor(
    @Named("io.pleo.test.prop8") val currency: Prop<CurrencyUnit>,
)
