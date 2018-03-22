package io.pleo.prop.archaius;

import java.util.function.Function;

import io.pleo.prop.core.Prop;
import io.pleo.prop.core.internal.PropFactory;

public class ArchaiusPropFactory implements PropFactory {

  @Override
  public <T> Prop<T> createProp(String propName, Function<String, T> parse, T defaultValue) {
    return new ArchaiusProp<>(new ParsingProperty<>(propName, parse, defaultValue));
  }
}
