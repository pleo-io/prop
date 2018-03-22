package io.pleo.prop.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.*;

@Retention(RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Default {
  /** The default value */
  String value() default "";
}
