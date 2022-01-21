package io.pleo.prop.core.internal

import io.pleo.prop.core.Parser
import java.lang.reflect.Type

@FunctionalInterface
interface ParserFactory {
    fun createParserForType(type: Type): Parser<*>
}
