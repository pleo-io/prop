package io.pleo.prop.objects;

import javax.inject.Inject;
import javax.inject.Named;

import io.pleo.prop.core.Default;
import io.pleo.prop.core.Prop;

public class InvalidDefaultValue {
  public static final String DEFAULT_VALUE = "This is not a double!";
  private Prop<Double> usesDefaultValue;

  @Inject
  public InvalidDefaultValue(@Default(DEFAULT_VALUE) @Named("io.pleo.undefined.property") Prop<Double> usesDefaultValue) {
    this.usesDefaultValue = usesDefaultValue;
  }

  public Prop<Double> getUsesDefaultValue() {
    return usesDefaultValue;
  }
}
