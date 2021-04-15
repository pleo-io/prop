package io.pleo.prop.core;

import java.time.Instant;
import java.util.function.Supplier;

/**
 * A Prop is a configured property for an application.
 * It is often dynamic and its value should not be cached.
 * <p>
 * Use .get() to get the value of the Prop
 *
 * @param <T> The type of the property.
 */
public interface Prop<T> extends Supplier<T> {
  /**
   * Get the name of the property
   *
   * @return the property name
   */
  String getName();

  /**
   * Kotlin convenience method to implement invocation
   * @return the property value
   */
  T invoke();

  /**
   * Gets the time when the property was last set/changed.
   *
   * @return the time when the property was last set/changed.
   */
  Instant getChangedTimestamp();

  /**
   * Add the callback to be triggered when the value of the property is
   * changed
   *
   * @param callback The callback that will be called when the property changes.
   */
  void addCallback(Runnable callback);

  /**
   * Remove all callbacks registered through the instance of property
   */
  void removeAllCallbacks();
}
