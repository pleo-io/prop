package io.pleo.prop.objects;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import io.pleo.prop.core.Prop;

import javax.inject.Named;

public class MyAssistedInjectFactoryImp {
  private final Prop<String> prop;
  private final String assistedArg;

  @Inject
  public MyAssistedInjectFactoryImp(
    @Named("io.pleo.test.prop3") Prop<String> prop,
    @Assisted String assistedArg
  ) {
    this.prop = prop;
    this.assistedArg = assistedArg;
  }
}
