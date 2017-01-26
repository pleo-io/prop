package io.pleo.prop.objects;

import javax.inject.Inject;
import javax.inject.Named;

import io.pleo.prop.core.Prop;

public class UsesTwiceSameProp {
  @Inject
  public UsesTwiceSameProp(@Named("io.pleo.test.prop3") Prop<String> stringProp1,
                           @Named("io.pleo.test.prop3") Prop<String> stringProp2) {

  }
}
