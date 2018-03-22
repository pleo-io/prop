package io.pleo.prop.objects;

import javax.inject.Inject;
import javax.inject.Named;

import io.pleo.prop.core.Default;
import io.pleo.prop.core.Prop;

public class InvalidDefaultValueButValidValue {
  public static final String DEFAULT_VALUE = "This is not a double!";
  private Prop<Double> usesDefaultValue;

  @Inject
  public InvalidDefaultValueButValidValue(@Default(DEFAULT_VALUE) @Named("io.pleo.test.prop6") Prop<Double> usesDefaultValue) {
    this.usesDefaultValue = usesDefaultValue;
  }

  public Prop<Double> getUsesDefaultValue() {
    return usesDefaultValue;
  }
}
