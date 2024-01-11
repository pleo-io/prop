package io.pleo.prop.objects

import io.pleo.prop.core.Prop
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider

class MyInterfaceProvider
@Inject
constructor(
    @Named("io.pleo.test.prop3") val prop: Prop<String>
) : Provider<MyInterface> {
    override fun get(): MyInterface =
        object : MyInterface {
            override val propValue = prop()
        }
}
