package io.pleo.prop.core;

import java.util.function.Supplier;

import io.pleo.prop.core.internal.LocalProp;

/**
 * Utility class to create Prop instances.
 */
public class Props {
  private Props() {
  }

  /**
   * Creates a static Prop that will always have the provided value.
   */
  public static <T> Prop<T> of(T value) {
    return new LocalProp<>(value);
  }

  /**
   * Creates a dynamic Prop that will evaluate the provided supplier every time it is read.
   */
  public static <T> Prop<T> of(Supplier<T> supplier) {
    return new LocalProp<>(supplier);
  }
}
