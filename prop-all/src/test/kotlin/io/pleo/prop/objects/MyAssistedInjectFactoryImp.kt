package io.pleo.prop.objects

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import io.pleo.prop.core.Prop
import javax.inject.Named

@Suppress("unused")
class MyAssistedInjectFactoryImp
    @Inject
    constructor(
        @Named("io.pleo.test.prop3") private val prop: Prop<String>,
        @Assisted private val assistedArg: String,
    )
