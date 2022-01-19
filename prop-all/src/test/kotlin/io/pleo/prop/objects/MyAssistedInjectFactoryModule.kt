package io.pleo.prop.objects

@Suppress("unused")
interface MyAssistedInjectFactoryModule {
    fun create(assistedArg: String): MyAssistedInjectFactoryImp
}
