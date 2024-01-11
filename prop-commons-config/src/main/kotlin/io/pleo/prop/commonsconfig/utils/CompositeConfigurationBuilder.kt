package io.pleo.prop.commonsconfig.utils

import org.apache.commons.configuration2.CompositeConfiguration
import org.apache.commons.configuration2.Configuration
import org.apache.commons.configuration2.builder.ConfigurationBuilder
import org.apache.commons.configuration2.builder.ConfigurationBuilderEvent
import org.apache.commons.configuration2.builder.ConfigurationBuilderResultCreatedEvent
import org.apache.commons.configuration2.event.Event
import org.apache.commons.configuration2.event.EventListener
import org.apache.commons.configuration2.event.EventListenerList
import org.apache.commons.configuration2.event.EventListenerRegistrationData
import org.apache.commons.configuration2.event.EventSource
import org.apache.commons.configuration2.event.EventType

class CompositeConfigurationBuilder : ConfigurationBuilder<CompositeConfiguration> {
    sealed interface BuilderOrConfiguration {
        fun isEqual(item: ConfigurationBuilder<*>) = this is BuilderWrapper && builder === item

        fun isEqual(item: Configuration) = this is ConfigurationWrapper && configuration === item
    }

    private class BuilderWrapper(val builder: ConfigurationBuilder<*>) : BuilderOrConfiguration

    private class ConfigurationWrapper(val configuration: Configuration) : BuilderOrConfiguration

    private lateinit var result: CompositeConfiguration
    private val isResultInitialised get() = ::result.isInitialized
    private val items = mutableListOf<BuilderOrConfiguration>()
    private val eventListeners: EventListenerList = EventListenerList()

    private fun ConfigurationBuilder<*>.wrap() = BuilderWrapper(this)

    private fun Configuration.wrap() = ConfigurationWrapper(this)

    private operator fun MutableList<BuilderOrConfiguration>.contains(item: ConfigurationBuilder<*>) = any { it.isEqual(item) }

    private operator fun MutableList<BuilderOrConfiguration>.contains(item: Configuration) = any { it.isEqual(item) }

    private fun MutableList<BuilderOrConfiguration>.remove(item: ConfigurationBuilder<*>) = removeIf { it.isEqual(item) }

    private fun MutableList<BuilderOrConfiguration>.remove(item: Configuration) = removeIf { it.isEqual(item) }

    fun add(item: ConfigurationBuilder<*>): Boolean =
        synchronized(items) {
            if (item !in items) {
                items.add(item.wrap())
            } else {
                false
            }
        }

    fun add(item: Configuration): Boolean =
        synchronized(items) {
            if (item !in items) {
                items.add(item.wrap())
            } else {
                false
            }
        }

    fun remove(item: ConfigurationBuilder<*>): Boolean = synchronized(items) { items.remove(item) }

    fun remove(item: Configuration): Boolean = synchronized(items) { items.remove(item) }

    override fun <T : Event> addEventListener(
        eventType: EventType<T>,
        listener: EventListener<in T>
    ) {
        eventListeners.addEventListener(eventType, listener)
        if (isResultInitialised) {
            result.addEventListener(eventType, listener)
        }
    }

    override fun <T : Event?> removeEventListener(
        eventType: EventType<T>?,
        listener: EventListener<in T>?
    ): Boolean {
        if (isResultInitialised) {
            result.removeEventListener(eventType, listener)
        }
        return eventListeners.removeEventListener(eventType, listener)
    }

    private fun fireBuilderEvent(event: ConfigurationBuilderEvent) {
        eventListeners.fire(event)
    }

    override fun getConfiguration(): CompositeConfiguration {
        fireBuilderEvent(ConfigurationBuilderEvent(this, ConfigurationBuilderEvent.CONFIGURATION_REQUEST))
        if (isResultInitialised) {
            return result
        }

        return createResult().also { compositeConfiguration ->
            items.forEach {
                val configuration =
                    when (it) {
                        is BuilderWrapper -> it.builder.configuration
                        is ConfigurationWrapper -> it.configuration
                    } as Configuration
                compositeConfiguration.addConfiguration(configuration)
            }

            result = compositeConfiguration

            fireBuilderEvent(
                ConfigurationBuilderResultCreatedEvent(
                    this,
                    ConfigurationBuilderResultCreatedEvent.RESULT_CREATED,
                    compositeConfiguration
                )
            )
        }
    }

    private fun <E : Event> registerListener(
        eventSource: EventSource,
        registrationData: EventListenerRegistrationData<E>
    ) {
        eventSource.addEventListener(registrationData.eventType, registrationData.listener)
    }

    private fun createResult(): CompositeConfiguration =
        CompositeConfiguration().apply {
            items.forEach {
                val configuration =
                    when (it) {
                        is BuilderWrapper -> it.builder.configuration
                        is ConfigurationWrapper -> it.configuration
                    } as Configuration
                addConfiguration(configuration)
            }

            eventListeners.registrations.forEach { registrationData ->
                registerListener(this, registrationData)
            }
        }
}
