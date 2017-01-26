package io.pleo.prop.guice.internal;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import io.pleo.prop.core.internal.ParserFactory;
import io.pleo.prop.jackson.FailedToParsePropValueException;

public class JacksonParserFactory implements ParserFactory {

  private final Map<Type, Function<String, Object>> knownParsers = new HashMap<>();

  private final ObjectMapper objectMapper;

  public JacksonParserFactory() {
    this(new ObjectMapper());
  }

  public JacksonParserFactory(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;

    // The list of Prop types that will not be parsed using Jackson and JSON parsing
    knownParsers.put(String.class, (String s) -> s);
    knownParsers.put(Boolean.class, Boolean::valueOf);
    knownParsers.put(Integer.class, Integer::valueOf);
    knownParsers.put(Float.class, Float::valueOf);
    knownParsers.put(Double.class, Double::valueOf);
    knownParsers.put(BigDecimal.class, BigDecimal::new);
    knownParsers.put(Duration.class, Duration::parse);
    knownParsers.put(Instant.class, Instant::parse);
  }

  /**
   * If you want to manually modify the list of known parsers.
   * Make sure you know what you're doing!
   */
  public Map<Type, Function<String, Object>> getKnownParsers() {
    return knownParsers;
  }

  @Override
  public Function<String, Object> createParserForType(Type type) {
    return knownParsers.computeIfAbsent(type, t -> rawValue -> parse(rawValue, t));
  }

  private Object parse(String rawValue, Type type) {
    {
      try {
        return objectMapper.readValue(rawValue, TypeFactory.defaultInstance().constructType(type));
      } catch (IOException | RuntimeException ex) {
        throw new FailedToParsePropValueException(ex);
      }
    }
  }
}
