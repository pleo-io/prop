package io.pleo.prop.guice.internal;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.google.inject.Binding;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.DefaultElementVisitor;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.Element;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.spi.PrivateElements;
import com.google.inject.spi.ProviderLookup;

import io.pleo.prop.core.Prop;
import io.pleo.prop.core.internal.ParserFactory;
import io.pleo.prop.core.internal.PropFactory;

/**
 * Goes over every binding and produces a Map associating Guice binding keys to
 * Pleo Prop instances.
 */
public class PropMappingVisitor extends DefaultElementVisitor<Map<Key<Prop<?>>, Prop<?>>> {
  private final InjectionPointExtractor injectionPointExtractor;
  private final PropFactory propFactory;
  private final ParserFactory parserFactory;

  public PropMappingVisitor(Predicate<TypeLiteral<?>> filter, PropFactory propFactory, ParserFactory parserFactory) {
    this.injectionPointExtractor = new InjectionPointExtractor(filter);
    this.propFactory = propFactory;
    this.parserFactory = parserFactory;
  }

  public Map<Key<Prop<?>>, Prop<?>> visit(Iterable<? extends Element> elements) {
    Map<Key<Prop<?>>, Prop<?>> mappings = new HashMap<>();

    for (Element element : elements) {
      Map<Key<Prop<?>>, Prop<?>> visitResult = element.acceptVisitor(this);
      // acceptVisitor returns null if the visitor is never called
      if (visitResult != null) {
        // We might encounter duplicates (multiple classes using the same property) but they will be overwritten.
        mappings.putAll(visitResult);
      }
    }

    return mappings;
  }

  @Override
  public Map<Key<Prop<?>>, Prop<?>> visit(PrivateElements privateElements) {
    return visit(privateElements.getElements());
  }

  @Override
  public <T> Map<Key<Prop<?>>, Prop<?>> visit(Binding<T> binding) {
    return extractProps(binding.acceptTargetVisitor(injectionPointExtractor));
  }

  @Override
  public <T> Map<Key<Prop<?>>, Prop<?>> visit(ProviderLookup<T> providerLookup) {
    return extractProps(providerLookup.getDependency().getInjectionPoint());
  }

  private Map<Key<Prop<?>>, Prop<?>> extractProps(InjectionPoint injectionPoint) {
    if (injectionPoint == null) {
      return new HashMap<>();
    }

    return injectionPoint
      .getDependencies()
      .stream()
      .map(Dependency::getKey)
      .filter(key -> key.getTypeLiteral().getRawType().equals(Prop.class) &&
                     key.getTypeLiteral().getType() instanceof ParameterizedType)
      .map(key -> (Key<Prop<?>>) key)
      .collect(Collectors.toMap(Function.identity(), this::keyToProp));
  }

  private Prop<?> keyToProp(Key<Prop<?>> key) {
    String propertyName = getNamedAnnotationValue(key);

    Type type = ((ParameterizedType) key.getTypeLiteral().getType()).getActualTypeArguments()[0];
    Function<String, Object> parser = parserFactory.createParserForType(type);

    try {
      return propFactory.createProp(propertyName, parser);
    } catch (RuntimeException ex) {
      throw new FailedToCreatePropException(propertyName, ex);
    }
  }

  private static String getNamedAnnotationValue(Key<Prop<?>> key) {
    String propName = null;

    // Both javax.inject.Named and com.google.inject.name.Named can be used with Guice.
    if (key.getAnnotation() instanceof javax.inject.Named) {
      propName = ((javax.inject.Named) key.getAnnotation()).value();
    } else if (key.getAnnotation() instanceof com.google.inject.name.Named) {
      propName = ((com.google.inject.name.Named) key.getAnnotation()).value();
    }

    if (Strings.isNullOrEmpty(propName)) {
      throw new RequiredNamedAnnotationException(key);
    }

    return propName;
  }
}
