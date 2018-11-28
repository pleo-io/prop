package io.pleo.prop.objects;

import javax.inject.Named;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import io.pleo.prop.core.Prop;

public class InlineProviderModule extends AbstractModule {
  @Provides
  @Singleton
  public InjectedObject hardwell(@Named("io.pleo.test.prop3") Prop<String> w_w) {
    return new InjectedObject();
  }

  @Provides
  @Singleton
  @Named("tiesto")
  public InjectedObject tiesto(InjectedObject io,
                               @Named("io.pleo.test.prop4") Prop<String> w_w,
                               @Named("io.pleo.test.prop3") Prop<String> u_u) {
    return new InjectedObject();
  }

}
