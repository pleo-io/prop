package io.pleo.prop.core.internal;

import java.util.function.Function;

import io.pleo.prop.core.Prop;

@FunctionalInterface
public interface PropFactory {
  <T> Prop<T> createProp(String propName, Function<String, T> parse);
}
