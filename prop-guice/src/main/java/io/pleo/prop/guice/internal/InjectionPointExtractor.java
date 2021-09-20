package io.pleo.prop.guice.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;
import com.google.inject.ConfigurationException;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.AssistedInjectBinding;
import com.google.inject.assistedinject.AssistedInjectTargetVisitor;
import com.google.inject.assistedinject.AssistedMethod;
import com.google.inject.spi.DefaultBindingTargetVisitor;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.spi.LinkedKeyBinding;
import com.google.inject.spi.ProviderKeyBinding;
import com.google.inject.spi.ProvidesMethodBinding;
import com.google.inject.spi.ProvidesMethodTargetVisitor;
import com.google.inject.spi.UntargettedBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InjectionPointExtractor extends DefaultBindingTargetVisitor<Object, Iterable<InjectionPoint>> implements
  ProvidesMethodTargetVisitor<Object, Iterable<InjectionPoint>>,
  AssistedInjectTargetVisitor<Object, Iterable<InjectionPoint>>
{

  private static final Logger logger = LoggerFactory.getLogger(InjectionPointExtractor.class);
  private final Predicate<TypeLiteral<?>> filter;

  public InjectionPointExtractor(Predicate<TypeLiteral<?>> filter) {
    this.filter = filter;
  }

  @Override
  public Iterable<InjectionPoint> visit(UntargettedBinding<?> untargettedBinding) {
    return Arrays.asList(getInjectionPointForKey(untargettedBinding.getKey()));
  }

  @Override
  public Iterable<InjectionPoint> visit(AssistedInjectBinding<?> assistedInjectBinding) {
    Collection<InjectionPoint> injectionPoints = new ArrayList<>();

    for (AssistedMethod assistedMethod : assistedInjectBinding.getAssistedMethods()) {
      try {
        injectionPoints.add(InjectionPoint.forConstructorOf(assistedMethod.getImplementationType()));
      } catch (ConfigurationException e) {
        logger.info("Skipping assistedMethod type {}: {}", assistedMethod.getImplementationType(), e.getMessage());
      }
    }

    return injectionPoints;
  }

  @Override
  public Iterable<InjectionPoint> visit(LinkedKeyBinding<?> linkedKeyBinding) {
    return  Arrays.asList(getInjectionPointForKey(linkedKeyBinding.getLinkedKey()));
  }

  @Override
  public Iterable<InjectionPoint> visit(ProviderKeyBinding<?> providerKeyBinding) {
    return  Arrays.asList(getInjectionPointForKey(providerKeyBinding.getProviderKey()));
  }

  @Override
  public Iterable<InjectionPoint> visit(ProvidesMethodBinding<?> providesMethodBinding) {
    TypeLiteral<?> type = TypeLiteral.get(providesMethodBinding.getEnclosingInstance().getClass());
    if(filter.test(type)){
      try {
        return  Arrays.asList(InjectionPoint.forMethod(providesMethodBinding.getMethod(),type));
      } catch (ConfigurationException e) {
        logger.info("Skipping key {}: {}", providesMethodBinding, e.getMessage());
        return null;
      }
    }
    return null;
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
