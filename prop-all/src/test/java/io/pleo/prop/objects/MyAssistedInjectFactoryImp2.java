package io.pleo.prop.objects;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import io.pleo.prop.core.Prop;

import javax.inject.Named;

public class MyAssistedInjectFactoryImp2 {
  private final Prop<String> prop;
  private final Integer assistedArg;

  @Inject
  public MyAssistedInjectFactoryImp2(
    @Named("io.pleo.test.prop4") Prop<String> prop,
    @Assisted Integer assistedArg
  ) {
    this.prop = prop;
    this.assistedArg = assistedArg;
  }
}
