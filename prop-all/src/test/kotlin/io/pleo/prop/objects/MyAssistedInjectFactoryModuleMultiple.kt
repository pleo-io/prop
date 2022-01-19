package io.pleo.prop.objects

@Suppress("unused")
interface MyAssistedInjectFactoryModuleMultiple {
    fun create(assistedArg: String): MyAssistedInjectFactoryImp
    fun create(assistedArg: Int): MyAssistedInjectFactoryImp2
}
