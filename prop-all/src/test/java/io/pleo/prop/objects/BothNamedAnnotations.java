package io.pleo.prop.objects;

import javax.inject.Inject;

import io.pleo.prop.core.Prop;

public class BothNamedAnnotations {
  private final Prop<String> stringProp1;
  private final Prop<String> stringProp2;

  @Inject
  public BothNamedAnnotations(@javax.inject.Named("io.pleo.test.prop3") Prop<String> stringProp1,
                              @com.google.inject.name.Named("io.pleo.test.prop4") Prop<String> stringProp2) {

    this.stringProp1 = stringProp1;
    this.stringProp2 = stringProp2;
  }

  public Prop<String> getStringProp1() {
    return stringProp1;
  }

  public Prop<String> getStringProp2() {
    return stringProp2;
  }
}
