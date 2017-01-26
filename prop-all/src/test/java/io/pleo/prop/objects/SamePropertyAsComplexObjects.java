package io.pleo.prop.objects;

import javax.inject.Inject;
import javax.inject.Named;

import io.pleo.prop.core.Prop;

public class SamePropertyAsComplexObjects {
  private Prop<InjectedObject> myComplexObjectProp;

  @Inject
  public SamePropertyAsComplexObjects(@Named("io.pleo.test.prop1") Prop<InjectedObject> myComplexObjectProp) {
    this.myComplexObjectProp = myComplexObjectProp;
  }

  public Prop<InjectedObject> getMyComplexObjectProp() {
    return myComplexObjectProp;
  }
}
