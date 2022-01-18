package io.pleo.prop.archaius

import com.google.common.collect.ImmutableMap
import com.google.common.truth.Truth.assertThat
import com.netflix.config.ConcurrentCompositeConfiguration
import com.netflix.config.ConfigurationManager
import com.netflix.config.DynamicConfiguration
import com.netflix.config.FixedDelayPollingScheduler
import com.netflix.config.PollResult
import com.netflix.config.PolledConfigurationSource
import org.apache.commons.configuration.AbstractConfiguration
import org.junit.Test
import java.util.function.Function

private const val PROPERTY_KEY = "ParsingPropertyTest_01"

class ParsingPropertyTest {
    private lateinit var propertyValue: String

    inner class TestConfigurationSource : PolledConfigurationSource {
        @Throws(Exception::class)
        override fun poll(initial: Boolean, checkPoint: Any?): PollResult =
            PollResult.createFull(ImmutableMap.of<String, Any>(PROPERTY_KEY, propertyValue))
    }

    @Test(expected = UndefinedPropertyException::class)
    fun undefined_property_throws() {
        ParsingProperty(PROPERTY_KEY, Function.identity(), null)
    }

    @Test
    @Throws(InterruptedException::class)
    fun can_change_value() {
        propertyValue = "value1"
        DynamicConfiguration(
            TestConfigurationSource(),
            FixedDelayPollingScheduler(0, 1, false),
        ).add()

        val property: ParsingProperty<String?> = ParsingProperty(PROPERTY_KEY, Function.identity(), null)
        assertThat(property.value).isEqualTo("value1")

        propertyValue = "value2"

        Thread.sleep(10)

        assertThat(property.value).isEqualTo("value2")
    }

    private fun AbstractConfiguration.add() =
        (ConfigurationManager.getConfigInstance() as ConcurrentCompositeConfiguration).addConfiguration(this)
}
