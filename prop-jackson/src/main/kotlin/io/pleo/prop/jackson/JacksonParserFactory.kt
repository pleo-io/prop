package io.pleo.prop.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import io.pleo.prop.core.Parser
import io.pleo.prop.core.internal.ParserFactory
import java.io.IOException
import java.lang.reflect.Type
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
import javax.money.Monetary

class JacksonParserFactory
@JvmOverloads
constructor(
    private val objectMapper: ObjectMapper = ObjectMapper(),
) : ParserFactory {
    @Suppress("MemberVisibilityCanBePrivate")
    private val knownParsers: MutableMap<Type, (String) -> Any> =
        mutableMapOf(
            // The list of Prop types that will not be parsed using Jackson and JSON parsing
            buildParserRef { it },
            buildParserRef(String::toBoolean),
            buildParserRef(String::toInt),
            buildParserRef(String::toFloat),
            buildParserRef(String::toDouble),
            buildParserRef(String::toBigDecimal),
            buildParserRef(Instant::parse),
            buildParserRef(OffsetDateTime::parse),
            buildParserRef(ZonedDateTime::parse),
            buildParserRef(Duration::parse),
            buildParserRef(LocalDateTime::parse),
            buildParserRef(LocalDate::parse),
            buildParserRef(LocalTime::parse),
            buildParserRef(MonthDay::parse),
            buildParserRef(OffsetTime::parse),
            buildParserRef(Period::parse),
            buildParserRef(Year::parse),
            buildParserRef(YearMonth::parse),
            buildParserRef(ZoneId::of),
            buildParserRef(ZoneOffset::of),
            buildParserRef(Monetary::getCurrency),
        )

    override fun createParserForType(type: Type): Parser<*> = knownParsers.getOrPut(type, buildParserBuilder(type))

    private fun <T : Any> buildParserBuilder(type: Type): (() -> Parser<T>) =
        {
            {
                    rawString: String ->
                try {
                    objectMapper.readValue(rawString, TypeFactory.defaultInstance().constructType(type))
                } catch (ex: IOException) {
                    throw FailedToParsePropValueException(ex)
                } catch (ex: RuntimeException) {
                    throw FailedToParsePropValueException(ex)
                }
            }
        }

    private inline fun <reified T : Any> buildParserRef(noinline parser: Parser<T>) = T::class.java to parser
}
