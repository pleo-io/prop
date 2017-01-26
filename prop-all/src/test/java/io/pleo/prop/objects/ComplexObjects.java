package io.pleo.prop.objects;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

import io.pleo.prop.core.Prop;

public class ComplexObjects {
  private Prop<InjectedObject> myComplexObjectProp;
  private Prop<InjectedObjectWithConstructor> withConstructor;
  private Prop<List<InjectedObject>> myListOfComplexObjectProp;
  private Prop<String> myStringProp;

  @Inject
  public ComplexObjects(@Named("io.pleo.test.prop1") Prop<InjectedObject> myComplexObjectProp,
                        @Named("io.pleo.test.prop1") Prop<InjectedObjectWithConstructor> withConstructor,
                        @Named("io.pleo.test.prop2") Prop<List<InjectedObject>> myListOfComplexObjectProp,
                        @Named("io.pleo.test.prop3") Prop<String> myStringProp) {
    this.myComplexObjectProp = myComplexObjectProp;
    this.withConstructor = withConstructor;
    this.myListOfComplexObjectProp = myListOfComplexObjectProp;
    this.myStringProp = myStringProp;
  }

  public Prop<InjectedObjectWithConstructor> getWithConstructor() {
    return withConstructor;
  }

  public Prop<InjectedObject> getMyComplexObjectProp() {
    return myComplexObjectProp;
  }

  public Prop<List<InjectedObject>> getMyListOfComplexObjectProp() {
    return myListOfComplexObjectProp;
  }

  public Prop<String> getMyStringProp() {
    return myStringProp;
  }
}
