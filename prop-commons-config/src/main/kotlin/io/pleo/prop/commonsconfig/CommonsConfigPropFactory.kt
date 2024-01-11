package io.pleo.prop.commonsconfig

import io.pleo.prop.core.Parser
import io.pleo.prop.core.Prop
import io.pleo.prop.core.internal.PropFactory
import org.apache.commons.configuration2.builder.ConfigurationBuilder
import org.apache.commons.configuration2.builder.fluent.Configurations
import java.io.File

class CommonsConfigPropFactory(
    private val builder: ConfigurationBuilder<*>? = null
) : PropFactory {
    override fun <T> createProp(
        propName: String,
        parse: Parser<T>,
        defaultValue: T?
    ): Prop<T> =
        CommonsConfigProp(
            ParsingProperty(
                builder = builder ?: createDefaultBuilder(),
                name = propName,
                parser = parse,
                defaultValue = defaultValue
            )
        )

    @Suppress("MemberVisibilityCanBePrivate")
    fun createDefaultBuilder(): ConfigurationBuilder<*> = Configurations().propertiesBuilder(File("config.properties"))
}
