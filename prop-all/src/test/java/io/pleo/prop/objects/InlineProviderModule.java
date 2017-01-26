package io.pleo.prop.objects;

import javax.inject.Named;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import io.pleo.prop.core.Prop;

public class InlineProviderModule extends AbstractModule {
  @Override
  protected void configure() {
  }

  @Provides
  @Singleton
  public InjectedObject hardwell(@Named("io.pleo.test.prop3") Prop<String> w_w) {
    return new InjectedObject();
  }
}
