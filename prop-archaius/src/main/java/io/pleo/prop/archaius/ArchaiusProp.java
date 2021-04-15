package io.pleo.prop.archaius;

import java.time.Instant;

import com.netflix.config.Property;

import io.pleo.prop.core.Prop;

/**
 * Prop that wraps a com.netflix.config.Property
 */
public class ArchaiusProp<T> implements Prop<T> {
  private Property<T> archaiusProperty;

  public ArchaiusProp(Property<T> archaiusProperty) {
    this.archaiusProperty = archaiusProperty;
  }

  @Override
  public T get() {
    return archaiusProperty.getValue();
  }

  @Override
  public T invoke() {
    return get();
  }

  @Override
  public String getName() {
    return archaiusProperty.getName();
  }

  @Override
  public Instant getChangedTimestamp() {
    return Instant.ofEpochMilli(archaiusProperty.getChangedTimestamp());
  }

  @Override
  public void addCallback(Runnable callback) {
    archaiusProperty.addCallback(callback);
  }

  @Override
  public void removeAllCallbacks() {
    archaiusProperty.removeAllCallbacks();
  }

  @Override
  public String toString() {
    return archaiusProperty.toString();
  }
}
