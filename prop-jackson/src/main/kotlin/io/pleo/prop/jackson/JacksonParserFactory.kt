package io.pleo.prop.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import io.pleo.prop.core.internal.ParserFactory
import java.io.IOException
import java.lang.reflect.Type
import java.math.BigDecimal
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.MonthDay
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.Period
import java.time.Year
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.function.Function

class JacksonParserFactory @JvmOverloads constructor(
    private val objectMapper: ObjectMapper = ObjectMapper(),
) : ParserFactory {
    /**
     * If you want to manually modify the list of known parsers.
     * Make sure you know what you're doing!
     */
    @Suppress("MemberVisibilityCanBePrivate")
    val knownParsers: MutableMap<Type, Function<String, Any>> = mutableMapOf(
        // The list of Prop types that will not be parsed using Jackson and JSON parsing
        String::class.java to Function { it },
        Boolean::class.java to Function(java.lang.Boolean::valueOf),
        Int::class.java to Function(Integer::valueOf),
        Float::class.java to Function(java.lang.Float::valueOf),
        Double::class.java to Function(java.lang.Double::valueOf),
        BigDecimal::class.java to Function(::BigDecimal),
        Instant::class.java to Function(Instant::parse),
        OffsetDateTime::class.java to Function(OffsetDateTime::parse),
        ZonedDateTime::class.java to Function(ZonedDateTime::parse),
        Duration::class.java to Function(Duration::parse),
        LocalDateTime::class.java to Function(LocalDateTime::parse),
        LocalDate::class.java to Function(LocalDate::parse),
        LocalTime::class.java to Function(LocalTime::parse),
        MonthDay::class.java to Function(MonthDay::parse),
        OffsetTime::class.java to Function(OffsetTime::parse),
        Period::class.java to Function(Period::parse),
        Year::class.java to Function(Year::parse),
        YearMonth::class.java to Function(YearMonth::parse),
        ZoneId::class.java to Function(ZoneId::of),
        ZoneOffset::class.java to Function(ZoneOffset::of),
    )

    /**
     * If you want to manually modify the list of known parsers.
     * Make sure you know what you're doing!
     */

    override fun createParserForType(type: Type): Function<String, Any> =
        knownParsers.computeIfAbsent(type) { _ ->
            Function { parse(it, type) }
        }

    private fun parse(rawValue: String, type: Type): Any =
        try {
            objectMapper.readValue<Any>(
                rawValue,
                TypeFactory.defaultInstance().constructType(type),
            )
        } catch (ex: IOException) {
            throw FailedToParsePropValueException(ex)
        } catch (ex: RuntimeException) {
            throw FailedToParsePropValueException(ex)
        }
}
