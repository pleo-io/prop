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
   *
   * @param value The static value of the Prop
   * @param <T>   The type of the Prop
   * @return A Prop instance that will always return the provided value.
   */
  public static <T> Prop<T> of(T value) {
    return new LocalProp<>(value);
  }

  /**
   * Creates a dynamic Prop that will evaluate the provided supplier every time it is read.
   *
   * @param supplier The Supplier that will be called when the Prop's value is read
   * @param <T>      The type of the Prop
   * @return A Prop instance that will always return the value returned by the provided supplier.
   */
  public static <T> Prop<T> of(Supplier<T> supplier) {
    return new LocalProp<>(supplier);
  }
}
