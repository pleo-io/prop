package io.pleo.prop.archaius;

import com.netflix.config.Property;

public class UndefinedPropertyException extends RuntimeException {
  public UndefinedPropertyException(Property<?> property) {
    super("Property '" + property.getName() + "' has a no value.");
  }
}
