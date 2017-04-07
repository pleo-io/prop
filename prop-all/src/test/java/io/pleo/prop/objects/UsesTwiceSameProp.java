package io.pleo.prop.objects;

import javax.inject.Inject;
import javax.inject.Named;

import io.pleo.prop.core.Prop;

public class UsesTwiceSameProp {
  private final Prop<String> stringProp1;
  private final Prop<String> stringProp2;

  @Inject
  public UsesTwiceSameProp(@Named("io.pleo.test.prop3") Prop<String> stringProp1,
                           @Named("io.pleo.test.prop3") Prop<String> stringProp2) {

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
