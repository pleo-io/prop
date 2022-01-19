package io.pleo.prop.core.internal

import java.lang.reflect.Type
import java.util.function.Function

@FunctionalInterface
interface ParserFactory {
    fun createParserForType(type: Type): Function<String, Any>
}
