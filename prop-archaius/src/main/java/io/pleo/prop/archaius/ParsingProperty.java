package io.pleo.prop.archaius;

import java.util.function.Function;

import com.netflix.config.PropertyWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParsingProperty<T> extends PropertyWrapper<T> {
  private static final Logger logger = LoggerFactory.getLogger(ParsingProperty.class);

  protected final Function<String, T> parser;
  private volatile T value;

  public ParsingProperty(String propName, Function<String, T> parser, T defaultValue) {
    super(propName, defaultValue);
    this.parser = parser;
    value = parseProperty();
  }

  private T parseProperty() {
    String stringValue = prop.getString();

    if (stringValue == null) {
      if (defaultValue == null) {
        throw new UndefinedPropertyException(this);
      }
      return defaultValue;
    }

    T parsedValue = parser.apply(stringValue);

    if (parsedValue == null) {
      throw new UndefinedPropertyException(this);
    }

    return parsedValue;
  }

  @Override
  protected final void propertyChanged() {
    try {
      T newValue = parseProperty();
      propertyChanged(getValue());
      logger.info("Property '{}' changed from '{}' to '{}'.", getName(), value, newValue);
      value = newValue;
    } catch (RuntimeException ex) {
      logger.warn("Failed to parse property '{}' with value '{}'. Keeping last valid value of '{}'.",
                  getName(),
                  prop.getString(),
                  value,
                  ex);
    }
  }

  @Override
  public T getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "{" + getName() + "=" + value + '}';
  }
}

