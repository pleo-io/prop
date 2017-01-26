package io.pleo.prop.objects;

import javax.inject.Inject;
import javax.inject.Named;

import io.pleo.prop.core.Prop;

public class InvalidJSON {
  @Inject
  public InvalidJSON(@Named("io.pleo.prop5") Prop<InjectedObject> object) {

  }
}
