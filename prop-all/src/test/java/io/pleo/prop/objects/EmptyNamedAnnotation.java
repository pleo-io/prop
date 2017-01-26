package io.pleo.prop.objects;

import javax.inject.Inject;
import javax.inject.Named;

import io.pleo.prop.core.Prop;

public class EmptyNamedAnnotation {
  @Inject
  public EmptyNamedAnnotation(@Named() Prop<String> myProp) {

  }
}
