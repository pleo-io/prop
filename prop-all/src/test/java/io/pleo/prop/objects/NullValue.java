package io.pleo.prop.objects;

import javax.inject.Inject;
import javax.inject.Named;

import io.pleo.prop.core.Prop;

public class NullValue {

  @Inject
  public NullValue(@Named("io.pleo.undefined.property") Prop<String> thisThrows) {
  }
}
