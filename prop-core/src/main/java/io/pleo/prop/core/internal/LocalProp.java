package io.pleo.prop.core.internal;

import java.time.Instant;
import java.util.function.Supplier;

import io.pleo.prop.core.Prop;

public class LocalProp<T> implements Prop<T> {

  private final Supplier<T> supplier;
  private final Instant created;

  public LocalProp(T value) {
    this(() -> value);
  }

  public LocalProp(Supplier<T> supplier) {
    this.supplier = supplier;
    this.created = Instant.now();
  }

  @Override
  public T get() {
    return supplier.get();
  }

  @Override
  public String getName() {
    return toString();
  }

  @Override
  public Instant getChangedTimestamp() {
    return created;
  }

  @Override
  public void addCallback(Runnable callback) {
    // Static properties can't change. No callbacks.
  }

  @Override
  public void removeAllCallbacks() {
    // Static properties can't change. No callbacks.
  }

  @Override
  public String toString() {
    return "LocalProp=" + get();
  }
}
