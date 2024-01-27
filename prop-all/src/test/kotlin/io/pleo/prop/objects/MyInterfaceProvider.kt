package io.pleo.prop.objects

import com.google.inject.Inject
import com.google.inject.name.Named
import io.pleo.prop.core.Prop
import jakarta.inject.Provider

class MyInterfaceProvider @Inject constructor(
    @Named("io.pleo.test.prop3") val prop: Prop<String>
) : Provider<MyInterface> {
    override fun get(): MyInterface =
        object : MyInterface {
            override val propValue = prop()
        }
}
