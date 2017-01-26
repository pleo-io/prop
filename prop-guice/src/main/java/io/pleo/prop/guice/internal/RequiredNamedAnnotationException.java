package io.pleo.prop.guice.internal;

import com.google.inject.Key;

import io.pleo.prop.core.Prop;
import io.pleo.prop.core.PropException;

public class RequiredNamedAnnotationException extends PropException {
  public RequiredNamedAnnotationException(Key<Prop<?>> key) {
    super("Property identified by key '" + key.toString() + "' has no @Named annotation.");
  }
}
