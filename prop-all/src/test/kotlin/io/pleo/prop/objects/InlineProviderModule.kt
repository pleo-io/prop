
package io.pleo.prop.objects

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import io.pleo.prop.core.Prop

@Suppress("unused", "SpellCheckingInspection", "unused_parameter")
class InlineProviderModule : AbstractModule() {
    @Provides
    @Singleton
    fun hardwell(
        @Named("io.pleo.test.prop3") w_w: Prop<String>,
    ): InjectedObject {
        return InjectedObject()
    }

    @Provides
    @Singleton
    @Named("tiesto")
    fun tiesto(
        io: InjectedObject?,
        @Named("io.pleo.test.prop4") w_w: Prop<String>,
        @Named("io.pleo.test.prop3") u_u: Prop<String>,
    ): InjectedObject {
        return InjectedObject()
    }
}
