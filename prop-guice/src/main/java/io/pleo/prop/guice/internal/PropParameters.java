package io.pleo.prop.guice.internal;

import java.lang.annotation.Annotation;
import java.util.List;

import com.google.inject.TypeLiteral;

public class PropParameters<T> {
  private TypeLiteral<T> typeLiteral;
  private List<Annotation> annotations;

  public PropParameters(TypeLiteral<T> typeLiteral, List<Annotation> annotations) {
    this.typeLiteral = typeLiteral;
    this.annotations = annotations;
  }

  public TypeLiteral<T> getTypeLiteral() {
    return typeLiteral;
  }

  public List<Annotation> getAnnotations() {
    return annotations;
  }
}
