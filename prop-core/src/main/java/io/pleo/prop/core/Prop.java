package io.pleo.prop.core;

import java.time.Instant;
import java.util.function.Supplier;

/**
 * A Prop is a configured property for an application.
 * It is often dynamic and its value should not be cached.
 * <p>
 * Use .get() to get the value of the Prop
 */
public interface Prop<T> extends Supplier<T> {
  /**
   * Get the name of the property
   *
   * @return the property name
   */
  String getName();

  /**
   * Gets the time when the property was last set/changed.
   */
  Instant getChangedTimestamp();

  /**
   * Add the callback to be triggered when the value of the property is
   * changed
   *
   * @param callback
   */
  void addCallback(Runnable callback);

  /**
   * remove all callbacks registered through the instance of property
   */
  void removeAllCallbacks();
}
