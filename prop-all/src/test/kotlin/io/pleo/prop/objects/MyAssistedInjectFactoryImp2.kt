package io.pleo.prop.objects

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import com.google.inject.name.Named
import io.pleo.prop.core.Prop

@Suppress("unused")
class MyAssistedInjectFactoryImp2
@Inject
constructor(
    @Named("io.pleo.test.prop4") private val prop: Prop<String>,
    @Assisted private val assistedArg: Int,
)
