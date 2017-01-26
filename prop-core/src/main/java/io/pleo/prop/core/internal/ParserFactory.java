package io.pleo.prop.core.internal;

import java.lang.reflect.Type;
import java.util.function.Function;

@FunctionalInterface
public interface ParserFactory {
  <T> Function<String, T> createParserForType(Type type);
}
