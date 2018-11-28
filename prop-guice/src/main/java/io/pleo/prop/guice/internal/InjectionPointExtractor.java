package io.pleo.prop.guice.internal;

import java.util.function.Predicate;

import com.google.inject.Binding;
import com.google.inject.ConfigurationException;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.ConstructorBinding;
import com.google.inject.spi.ConvertedConstantBinding;
import com.google.inject.spi.DefaultBindingTargetVisitor;
import com.google.inject.spi.ExposedBinding;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.spi.InstanceBinding;
import com.google.inject.spi.LinkedKeyBinding;
import com.google.inject.spi.ProviderBinding;
import com.google.inject.spi.ProviderInstanceBinding;
import com.google.inject.spi.ProviderKeyBinding;
import com.google.inject.spi.ProvidesMethodBinding;
import com.google.inject.spi.ProvidesMethodTargetVisitor;
import com.google.inject.spi.UntargettedBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InjectionPointExtractor extends DefaultBindingTargetVisitor<Object, InjectionPoint> implements
                                                                                                 ProvidesMethodTargetVisitor<Object, InjectionPoint>  {
  private static final Logger logger = LoggerFactory.getLogger(InjectionPointExtractor.class);
  private final Predicate<TypeLiteral<?>> filter;

  public InjectionPointExtractor(Predicate<TypeLiteral<?>> filter) {
    this.filter = filter;
  }

  @Override
  public InjectionPoint visit(UntargettedBinding<?> untargettedBinding) {
    return getInjectionPointForKey(untargettedBinding.getKey());
  }

  @Override
  public InjectionPoint visit(LinkedKeyBinding<?> linkedKeyBinding) {
    return getInjectionPointForKey(linkedKeyBinding.getLinkedKey());
  }

  @Override
  public InjectionPoint visit(ProviderKeyBinding<?> providerKeyBinding) {
    return getInjectionPointForKey(providerKeyBinding.getProviderKey());
  }

  @Override
  public InjectionPoint visit(ProvidesMethodBinding<?> providesMethodBinding) {
    return providesMethodBinding.getDependencies().iterator().next().getInjectionPoint();
   // return getInjectionPointForKey(providesMethodBinding.getKey());
  }

  private InjectionPoint getInjectionPointForKey(Key<?> key) {
    if (filter.test(key.getTypeLiteral())) {
      try {
        return InjectionPoint.forConstructorOf(key.getTypeLiteral());
      } catch (ConfigurationException e) {
        logger.info("Skipping key {}: {}", key, e.getMessage());
        return null;
      }
    }
    return null;
  }

}
