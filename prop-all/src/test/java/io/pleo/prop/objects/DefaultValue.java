package io.pleo.prop.objects;

import javax.inject.Inject;
import javax.inject.Named;

import io.pleo.prop.core.Default;
import io.pleo.prop.core.Prop;

public class DefaultValue {
  public static final String DEFAULT_VALUE = "This is the default value!";
  private Prop<String> usesDefaultValue;

  @Inject
  public DefaultValue(@Default(DEFAULT_VALUE) @Named("io.pleo.undefined.property") Prop<String> usesDefaultValue) {
    this.usesDefaultValue = usesDefaultValue;
  }

  public Prop<String> getUsesDefaultValue() {
    return usesDefaultValue;
  }
}
