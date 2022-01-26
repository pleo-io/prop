package io.pleo.prop.commonsconfig.utils

import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import org.apache.commons.configuration2.PropertiesConfiguration
import org.apache.commons.configuration2.builder.BasicConfigurationBuilder
import org.apache.commons.configuration2.event.ConfigurationEvent
import org.apache.commons.configuration2.event.EventListener
import org.junit.jupiter.api.Test

class CompositeConfigurationBuilderTest {
    private val testKey = "c-minor"
    private val testValueA = "1 million dollar"
    private val testValueB = "1 bazillion dollar"

    @Test
    fun `added event listeners are also added to configuration`() {
        val listener1 = mockk<EventListener<ConfigurationEvent>>()
        val listener2 = mockk<EventListener<ConfigurationEvent>>()

        val builder = CompositeConfigurationBuilder()
        builder.addEventListener(ConfigurationEvent.ANY, listener1)

        val config = builder.configuration

        builder.addEventListener(ConfigurationEvent.ANY, listener2)

        val listeners = config.getEventListeners(ConfigurationEvent.ANY)
        assertThat(listeners).containsExactly(listener1, listener2)
    }

    @Test
    fun `can get property value`() {
        val builder = CompositeConfigurationBuilder()
        val innerConfig = buildBasicBuilder().configuration
        innerConfig.addProperty(testKey, testValueA)
        builder.add(innerConfig)
        val outerConfig = builder.configuration

        val actual = outerConfig.getString(testKey)

        assertThat(actual).isEqualTo(testValueA)
    }

    @Test
    fun `gets property value from nested builder`() {
        val builder = CompositeConfigurationBuilder()
        val innerBuilder = buildBasicBuilder()
        innerBuilder.configuration.addProperty(testKey, testValueA)
        builder.add(innerBuilder)

        val actual = builder.configuration.getString(testKey)

        assertThat(actual).isEqualTo(testValueA)
    }

    @Test
    fun `gets property value from first matching configuration`() {
        val builder = CompositeConfigurationBuilder()

        val innerConfigA = PropertiesConfiguration()
        innerConfigA.addProperty(testKey, testValueA)
        builder.add(innerConfigA)

        val innerConfigB = PropertiesConfiguration()
        innerConfigB.addProperty(testKey, testValueB)
        builder.add(innerConfigB)

        val actual = builder.configuration.getString(testKey)

        assertThat(actual).isEqualTo(testValueA)
    }

    private fun buildBasicBuilder() = BasicConfigurationBuilder(PropertiesConfiguration::class.java)
}
