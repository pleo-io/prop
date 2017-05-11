package io.pleo.prop.guice.internal;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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

    knownParsers.put(Instant.class, Instant::parse);
    knownParsers.put(OffsetDateTime.class, OffsetDateTime::parse);
    knownParsers.put(ZonedDateTime.class, ZonedDateTime::parse);
    knownParsers.put(Duration.class, Duration::parse);
    knownParsers.put(LocalDateTime.class, LocalDateTime::parse);
    knownParsers.put(LocalDate.class, LocalDate::parse);
    knownParsers.put(LocalTime.class, LocalTime::parse);
    knownParsers.put(MonthDay.class, MonthDay::parse);
    knownParsers.put(OffsetTime.class, OffsetTime::parse);
    knownParsers.put(Period.class, Period::parse);
    knownParsers.put(Year.class, Year::parse);
    knownParsers.put(YearMonth.class, YearMonth::parse);
    knownParsers.put(ZoneId.class, ZoneId::of);
    knownParsers.put(ZoneOffset.class, ZoneOffset::of);
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
