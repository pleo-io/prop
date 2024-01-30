package io.pleo.prop.guice.internal

import com.google.inject.TypeLiteral

class PropParameters<T>(
    val typeLiteral: TypeLiteral<T>,
    val annotations: List<Annotation>,
)
