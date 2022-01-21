package io.pleo.prop.archaius

import com.netflix.config.PropertyWrapper
import io.pleo.prop.core.Parser
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger(ParsingProperty::class.java)

class ParsingProperty<T>(
    propName: String,
    private val parser: Parser<T>,
    defaultValue: T?,
) : PropertyWrapper<T>(propName, defaultValue) {
    @Volatile
    private var value: T = parseProperty()

    private fun parseProperty(): T {
        val stringValue = prop.string
        if (stringValue == null) {
            if (defaultValue == null) {
                throw UndefinedPropertyException(this)
            }
            return defaultValue
        }
        return parser(stringValue) ?: throw UndefinedPropertyException(this)
    }

    override fun propertyChanged() =
        try {
            val newValue = parseProperty()
            propertyChanged(getValue())
            logger.info("Property '{}' changed.", name)
            value = newValue
        } catch (ex: RuntimeException) {
            logger.warn(
                "Failed to parse property '{}'. Keeping last valid value.",
                name,
                ex
            )
        }

    override fun getValue(): T = value
    override fun toString(): String = "{$name=$value}"
}
