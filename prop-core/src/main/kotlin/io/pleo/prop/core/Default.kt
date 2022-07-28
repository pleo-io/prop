package io.pleo.prop.core

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Default(
    /** The default value  */
    val value: String = ""
)
