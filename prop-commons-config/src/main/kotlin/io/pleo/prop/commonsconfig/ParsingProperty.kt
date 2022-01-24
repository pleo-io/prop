package io.pleo.prop.commonsconfig

import io.pleo.prop.core.Callback
import io.pleo.prop.core.Parser
import org.apache.commons.configuration2.builder.ConfigurationBuilder
import org.apache.commons.configuration2.builder.ConfigurationBuilderEvent
import org.apache.commons.configuration2.event.ConfigurationEvent
import org.slf4j.LoggerFactory
import java.time.Instant

private val logger = LoggerFactory.getLogger(ParsingProperty::class.java)

class ParsingProperty<T>(
    private val builder: ConfigurationBuilder<*>,
    val name: String,
    private val parser: Parser<T>,
    private val defaultValue: T?,
) {
    @Volatile
    private var value: T = parseProperty()
    private val callbacks = mutableListOf<Callback<T>>()

    var changedTimestamp: Instant = Instant.now()
        private set

    private fun parseProperty(): T {
        val stringValue =
            try {
                builder.configuration.getString(name)
            } catch (ex: Exception) {
                logger.error("Failed to load string property $name", ex)
                throw ex
            }
                ?: return defaultValue
                    ?: throw UndefinedPropertyException(this)

        return parser(stringValue) ?: throw UndefinedPropertyException(this)
    }

    init {
        builder.addEventListener(ConfigurationBuilderEvent.RESET) { propertyChanged() }
        builder.addEventListener(ConfigurationEvent.ANY) { event ->
            if (!event.isBeforeUpdate && (event.propertyName == null || event.propertyName == name)) {
                propertyChanged()
            }
        }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun propertyChanged() {
        try {
            val newValue = parseProperty()
            if (newValue == value) {
                return
            }

            changedTimestamp = Instant.now()
            logger.info("Property '{}' changed.", name)
            if (callbacks.isNotEmpty()) {
                callbacks.forEach { it(newValue) }
            }
            value = newValue
        } catch (ex: RuntimeException) {
            logger.warn(
                "Failed to parse property '{}'. Keeping last valid value.",
                name,
                ex
            )
        }
    }

    fun getValue(): T = value

    fun addCallback(callback: Callback<T>) {
        callbacks.add(callback)
    }

    fun removeAllCallbacks() {
        callbacks.clear()
    }

    override fun toString(): String = "{$name=$value}"
}
