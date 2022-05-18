package io.pleo.prop.objects

import io.pleo.prop.core.Prop
import javax.inject.Inject
import javax.inject.Named
import javax.money.CurrencyUnit

@Suppress("unused_parameter")
class CurrencyUnitProp @Inject constructor(@Named("io.pleo.test.prop8") val currency: Prop<CurrencyUnit>)
