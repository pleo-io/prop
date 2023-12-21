package io.pleo.prop.commonsconfig

import com.google.common.truth.Truth.assertThat
import org.apache.commons.configuration2.BaseConfiguration
import org.apache.commons.configuration2.PropertiesConfiguration
import org.apache.commons.configuration2.builder.BasicConfigurationBuilder
import org.apache.commons.configuration2.builder.fluent.Parameters
import org.apache.commons.configuration2.reloading.ReloadingController
import org.apache.commons.configuration2.reloading.ReloadingDetector
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

private const val PROPERTY_KEY = "ParsingPropertyTest_01"
private lateinit var propertyValue: String

class ParsingPropertyTest {
    class TestConfiguration : BaseConfiguration() {
        override fun getPropertyInternal(key: String?): Any = propertyValue
    }

    @Test
    fun `undefined property throws`() {
        val builder = BasicConfigurationBuilder(PropertiesConfiguration::class.java, emptyMap(), false)

        builder.configure(Parameters().basic().setThrowExceptionOnMissing(false))
        assertThrows<UndefinedPropertyException> {
            ParsingProperty(builder, PROPERTY_KEY, { it }, null)
        }
    }

    @Test
    fun `can change value`() {
        propertyValue = "value1"

        val reloadingDetector =
            object : ReloadingDetector {
                override fun isReloadingRequired(): Boolean = true

                override fun reloadingPerformed() = Unit
            }
        val reloadingController = ReloadingController(reloadingDetector)

        val builder = BasicConfigurationBuilder(TestConfiguration::class.java, emptyMap(), false)
        builder.configuration.addProperty(PROPERTY_KEY, propertyValue)
        builder.connectToReloadingController(reloadingController)

        val property: ParsingProperty<String?> = ParsingProperty(builder, PROPERTY_KEY, { it }, null)
        assertThat(property.getValue()).isEqualTo("value1")

        propertyValue = "value2"

        reloadingController.checkForReloading(null)

        assertThat(property.getValue()).isEqualTo("value2")
    }
}
