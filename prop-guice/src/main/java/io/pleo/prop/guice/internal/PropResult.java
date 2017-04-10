package io.pleo.prop.guice.internal;

import io.pleo.prop.core.Prop;

public class PropResult {
  private Prop<?> prop;
  private Throwable error;

  public PropResult(Prop<?> prop) {
    this.prop = prop;
  }

  public PropResult(Throwable error) {
    this.error = error;
  }

  public Prop<?> getProp() {
    return prop;
  }

  public Throwable getError() {
    return error;
  }

  public boolean isError() {
    return error != null;
  }
}
