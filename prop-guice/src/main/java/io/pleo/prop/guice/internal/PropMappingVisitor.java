package io.pleo.prop.guice.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Member;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

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

import io.pleo.prop.core.Default;
import io.pleo.prop.core.Prop;
import io.pleo.prop.core.internal.ParserFactory;
import io.pleo.prop.core.internal.PropFactory;

/**
 * Goes over every binding and produces a Map associating Guice binding keys to
 * Pleo Prop instances.
 */
public class PropMappingVisitor extends DefaultElementVisitor<Map<Key<Prop<?>>, PropResult>> {
  private final InjectionPointExtractor injectionPointExtractor;
  private final PropFactory propFactory;
  private final ParserFactory parserFactory;

  public PropMappingVisitor(Predicate<TypeLiteral<?>> filter, PropFactory propFactory, ParserFactory parserFactory) {
    this.injectionPointExtractor = new InjectionPointExtractor(filter);
    this.propFactory = propFactory;
    this.parserFactory = parserFactory;
  }

  public Map<Key<Prop<?>>, PropResult> visit(Iterable<? extends Element> elements) {
    Map<Key<Prop<?>>, PropResult> mappings = new HashMap<>();

    for (Element element : elements) {
      Map<Key<Prop<?>>, PropResult> visitResult = element.acceptVisitor(this);
      // acceptVisitor returns null if the visitor is never called
      if (visitResult != null) {
        // We might encounter duplicates (multiple classes using the same property) but they will be overwritten.
        mappings.putAll(visitResult);
      }
    }

    return mappings;
  }

  @Override
  public Map<Key<Prop<?>>, PropResult> visit(PrivateElements privateElements) {
    return visit(privateElements.getElements());
  }

  @Override
  public <T> Map<Key<Prop<?>>, PropResult> visit(Binding<T> binding) {
    return extractProps(binding.acceptTargetVisitor(injectionPointExtractor));
  }

  @Override
  public <T> Map<Key<Prop<?>>, PropResult> visit(ProviderLookup<T> providerLookup) {
    return extractProps(providerLookup.getDependency().getInjectionPoint());
  }

  private Map<Key<Prop<?>>, PropResult> extractProps(InjectionPoint injectionPoint) {
    if (injectionPoint == null) {
      return new HashMap<>();
    }

    Map<Key<Prop<?>>, PropResult> extractedProps = new HashMap<>();
    Member injectionPointMember = injectionPoint.getMember();
    if (injectionPointMember instanceof Executable) {
      List<Parameter> parameters = Arrays.asList(((Executable) injectionPointMember).getParameters());
      for (Dependency<?> dependency : injectionPoint.getDependencies()) {
        Key<?> key = dependency.getKey();
        if (key.getTypeLiteral().getRawType().equals(Prop.class) &&
            key.getTypeLiteral().getType() instanceof ParameterizedType) {
          Parameter parameter = parameters.get(dependency.getParameterIndex());
          PropResult result;
          try {
            result = new PropResult(parameterToProp(parameter, key));
          } catch (RuntimeException ex) {
            result = new PropResult(ex);
          }
          extractedProps.put(((Key<Prop<?>>) dependency.getKey()), result);
        }
      }
    }

    // TODO - GSIMARD: Remove commented code
    //    injectionPoint
    //      .getDependencies()
    //      .stream()
    //      .map(Dependency::getKey)
    //      .filter(key -> key.getTypeLiteral().getRawType().equals(Prop.class) &&
    //                     key.getTypeLiteral().getType() instanceof ParameterizedType)
    //      .map(key -> (Key<Prop<?>>) key)
    //      .forEach(key -> {
    //        try {
    //          Prop<?> value = keyToProp(key);
    //          extractedProps.put(key, new PropResult(value));
    //        } catch (RuntimeException ex) {
    //          extractedProps.put(key, new PropResult(ex));
    //        }
    //      });
    return extractedProps;
  }

  private Prop<?> parameterToProp(Parameter parameter, Key key) {
    String propertyName = getNamedAnnotationValue(Arrays.asList(parameter.getAnnotations()), key);

    Type type = ((ParameterizedType) parameter.getParameterizedType()).getActualTypeArguments()[0];
    Function<String, Object> parser = parserFactory.createParserForType(type);

    try {
      Object defaultValue = Optional
        .ofNullable(parameter.getAnnotation(Default.class))
        .map(Default::value)
        .map(parser::apply)
        .orElse(null);
      return propFactory.createProp(propertyName, parser, defaultValue);
    } catch (RuntimeException ex) {
      throw new FailedToCreatePropException(propertyName, ex);
    }
  }

  //  private Prop<?> keyToProp(Key<Prop<?>> key) {
  //    String propertyName = getNamedAnnotationValue(key);
  //
  //    Type type = ((ParameterizedType) key.getTypeLiteral().getType()).getActualTypeArguments()[0];
  //    Function<String, Object> parser = parserFactory.createParserForType(type);
  //
  //    String rawDefaultValue = getDefaultAnnotationValue(key);
  //    try {
  //      Object defaultValue = rawDefaultValue == null ? null : parser.apply(rawDefaultValue);
  //      return propFactory.createProp(propertyName, parser, defaultValue);
  //    } catch (RuntimeException ex) {
  //      throw new FailedToCreatePropException(propertyName, ex);
  //    }
  //  }
  //
  //  private static String getNamedAnnotationValue(Key<Prop<?>> key) {
  //    String propName = null;
  //
  //    // Both javax.inject.Named and com.google.inject.name.Named can be used with Guice.
  //    if (key.getAnnotation() instanceof javax.inject.Named) {
  //      propName = ((javax.inject.Named) key.getAnnotation()).value();
  //    } else if (key.getAnnotation() instanceof com.google.inject.name.Named) {
  //      propName = ((com.google.inject.name.Named) key.getAnnotation()).value();
  //    }
  //
  //    if (Strings.isNullOrEmpty(propName)) {
  //      throw new RequiredNamedAnnotationException(key);
  //    }
  //
  //    return propName;
  //  }

  private static String getNamedAnnotationValue(List<Annotation> annotations, Key key) {
    String propName = null;

    // TODO - GSIMARD: Rework with filter and findFirst
    for (Annotation annotation : annotations) {
      // Both javax.inject.Named and com.google.inject.name.Named can be used with Guice.
      if (annotation instanceof javax.inject.Named) {
        propName = ((javax.inject.Named) annotation).value();
      } else if (annotation instanceof com.google.inject.name.Named) {
        propName = ((com.google.inject.name.Named) annotation).value();
      }
    }

    if (Strings.isNullOrEmpty(propName)) {
      throw new RequiredNamedAnnotationException(key);
    }
    return propName;
  }

  private static String getDefaultAnnotationValue(List<Annotation> annotations) {
    for (Annotation annotation : annotations) {
      if (annotations instanceof Default) {
        return Strings.emptyToNull(((Default) annotation).value());
      }
    }
    return null;
  }
}
