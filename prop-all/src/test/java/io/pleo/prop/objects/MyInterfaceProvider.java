package io.pleo.prop.objects;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import io.pleo.prop.core.Prop;

public class MyInterfaceProvider implements Provider<MyInterface> {
  private final Prop<String> prop;

  public Prop<String> getProp() {
    return prop;
  }

  @Inject
  public MyInterfaceProvider(@Named("io.pleo.test.prop3") Prop<String> prop) {
    this.prop = prop;
  }

  @Override

  public MyInterface get() {
    return prop::get;
  }
}
