package io.pleo.prop.guice.internal;

import io.pleo.prop.core.PropException;

public class FailedToCreatePropException extends PropException {
  public FailedToCreatePropException(String propName, Throwable cause) {
    super("Failed to create prop '" + propName + "'.", cause);
  }
}
