package io.pleo.prop.core

typealias Callback<T> = (T) -> Unit
typealias Supplier<T> = () -> T
typealias Parser<T> = (String) -> T
