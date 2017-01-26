package io.pleo.prop.core;

public class PropException extends RuntimeException {
  public PropException(String message) {
    super(message);
  }

  public PropException(String message, Throwable cause) {
    super(message, cause);
  }

  public PropException(Throwable cause) {
    super(cause);
  }
}
