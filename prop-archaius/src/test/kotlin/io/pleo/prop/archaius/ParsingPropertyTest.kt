package io.pleo.prop.archaius

import com.google.common.collect.ImmutableMap
import com.google.common.truth.Truth.assertThat
import com.netflix.config.ConcurrentCompositeConfiguration
import com.netflix.config.ConfigurationManager
import com.netflix.config.DynamicConfiguration
import com.netflix.config.FixedDelayPollingScheduler
import com.netflix.config.PollResult
import com.netflix.config.PolledConfigurationSource
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.function.Function

private const val PROPERTY_KEY = "ParsingPropertyTest_01"

class ParsingPropertyTest {
    private lateinit var propertyValue: String

    inner class TestConfigurationSource : PolledConfigurationSource {
        override fun poll(initial: Boolean, checkPoint: Any?): PollResult =
            PollResult.createFull(ImmutableMap.of<String, Any>(PROPERTY_KEY, propertyValue))
    }

    @Test
    fun `undefined property throws`() {
        assertThrows<UndefinedPropertyException> {
            ParsingProperty(PROPERTY_KEY, Function.identity(), null)
        }
    }

    @Test
    fun `can change value`() {
        propertyValue = "value1"
        val configuration = ConfigurationManager.getConfigInstance() as ConcurrentCompositeConfiguration
        configuration.addConfiguration(
            DynamicConfiguration(
                TestConfigurationSource(),
                FixedDelayPollingScheduler(0, 1, false),
            )
        )

        val property: ParsingProperty<String?> = ParsingProperty(PROPERTY_KEY, Function.identity(), null)
        assertThat(property.value).isEqualTo("value1")

        propertyValue = "value2"

        Thread.sleep(10)

        assertThat(property.value).isEqualTo("value2")
        configuration.clear()
    }
}
