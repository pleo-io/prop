package io.pleo.prop.jackson;

import io.pleo.prop.core.PropException;

public class FailedToParsePropValueException extends PropException {
  public FailedToParsePropValueException(Throwable cause) {
    super("Failed to parse property value.", cause);
  }
}
