package io.pleo.prop

import com.google.common.truth.Truth.assertThat
import com.google.inject.Binder
import com.google.inject.CreationException
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Module
import com.google.inject.PrivateModule
import com.google.inject.assistedinject.FactoryModuleBuilder
import io.mockk.mockk
import io.pleo.prop.commonsconfig.CommonsConfigPropFactory
import io.pleo.prop.guice.AutoPropModule
import io.pleo.prop.guice.internal.FailedToCreatePropException
import io.pleo.prop.guice.internal.RequiredNamedAnnotationException
import io.pleo.prop.jackson.JacksonParserFactory
import io.pleo.prop.objects.BothNamedAnnotations
import io.pleo.prop.objects.ComplexObjects
import io.pleo.prop.objects.DefaultValue
import io.pleo.prop.objects.EmptyNamedAnnotation
import io.pleo.prop.objects.InjectedObject
import io.pleo.prop.objects.InlineProviderModule
import io.pleo.prop.objects.InvalidDefaultValue
import io.pleo.prop.objects.InvalidDefaultValueButValidValue
import io.pleo.prop.objects.InvalidJSON
import io.pleo.prop.objects.MyAssistedInjectFactoryModule
import io.pleo.prop.objects.MyAssistedInjectFactoryModuleMultiple
import io.pleo.prop.objects.MyInterface
import io.pleo.prop.objects.MyInterfaceProvider
import io.pleo.prop.objects.NoPropObject
import io.pleo.prop.objects.NullValue
import io.pleo.prop.objects.SamePropertyAsComplexObjects
import io.pleo.prop.objects.UnnamedProp
import io.pleo.prop.objects.UsesTwiceSameProp
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import javax.sql.DataSource

class PropTest {
    @Test
    fun `can read complex properties`() {
        val injector = createInjector(
            Module { binder: Binder ->
                binder.bind(ComplexObjects::class.java)
            }
        )

        val complexObjects = injector.getInstance(ComplexObjects::class.java)
        val complexObjectPropValue = complexObjects.myComplexObjectProp()
        assertThat(complexObjectPropValue.name).isEqualTo("Rush B")
        assertThat(complexObjectPropValue.age).isEqualTo(12)

        val listOfComplexObjectPropValue = complexObjects.myListOfComplexObjectProp()
        assertThat(listOfComplexObjectPropValue).hasSize(2)
        assertThat(listOfComplexObjectPropValue[0].name).isEqualTo("dustII")
        assertThat(listOfComplexObjectPropValue[0].age).isEqualTo(3)
        assertThat(listOfComplexObjectPropValue[1].name).isEqualTo("inferno")
        assertThat(listOfComplexObjectPropValue[1].age).isEqualTo(16)

        val stringPropValue = complexObjects.myStringProp()
        assertThat(stringPropValue).isEqualTo("awp")
    }

    @Test
    fun `can bind non prop objects`() {
        val dataSource = mockk<DataSource>()
        val injector = createInjector(
            Module { binder: Binder ->
                binder.bind(DataSource::class.java).toInstance(dataSource)
                binder.bind(NoPropObject::class.java)
            }
        )

        val actual = injector.getInstance(NoPropObject::class.java)

        assertThat(actual.dataSource).isEqualTo(dataSource)
    }

    @Test
    fun `throws on null values`() {
        assertThrows<FailedToCreatePropException> {
            val injector = createInjector(
                Module { binder: Binder ->
                    binder.bind(
                        NullValue::class.java
                    )
                }
            )

            injector.getInstance(NullValue::class.java)
        }
    }

    @Test
    fun `uses default value on missing value`() {
        val injector = createInjector(
            Module { binder: Binder ->
                binder.bind(
                    DefaultValue::class.java
                )
            }
        )

        val defaultValue = injector.getInstance(DefaultValue::class.java)

        assertThat(defaultValue.usesDefaultValue()).isEqualTo(DefaultValue.DEFAULT_VALUE)
    }

    @Test
    fun `throws on unnamed prop`() {
        assertThrows<RequiredNamedAnnotationException> {
            val injector = createInjector(
                Module { binder: Binder ->
                    binder.bind(UnnamedProp::class.java)
                }
            )

            injector.getInstance(UnnamedProp::class.java)
        }
    }

    @Test
    fun `throws on invalid default value`() {
        assertThrows<FailedToCreatePropException> {
            val injector = createInjector(
                Module { binder: Binder ->
                    binder.bind(InvalidDefaultValue::class.java)
                }
            )

            injector.getInstance(InvalidDefaultValue::class.java)
        }
    }

    @Test
    fun `throws on invalid default value even if there is a valid value in config`() {
        assertThrows<FailedToCreatePropException> {
            val injector = createInjector(
                Module { binder: Binder ->
                    binder.bind(InvalidDefaultValueButValidValue::class.java)
                }
            )

            injector.getInstance(InvalidDefaultValueButValidValue::class.java)
        }
    }

    @Test
    fun `can have multiple objects using the same prop`() {
        val injector = createInjector(
            Module { binder: Binder ->
                binder.bind(ComplexObjects::class.java)
                binder.bind(SamePropertyAsComplexObjects::class.java)
            }
        )

        val complexObjects = injector.getInstance(ComplexObjects::class.java)
        val samePropertyAsComplexObjects = injector.getInstance(SamePropertyAsComplexObjects::class.java)

        assertThat(complexObjects.myComplexObjectProp)
            .isSameInstanceAs(samePropertyAsComplexObjects.myComplexObjectProp)
    }

    @Test
    fun `can use twice same prop in same object`() {
        val injector = createInjector(
            Module { binder: Binder ->
                binder.bind(
                    UsesTwiceSameProp::class.java
                )
            }
        )

        val samePropTwice = injector.getInstance(UsesTwiceSameProp::class.java)

        assertThat(samePropTwice.stringProp1()).isEqualTo(samePropTwice.stringProp2())
    }

    @Test
    fun `can use both named annotations`() {
        val injector = createInjector(
            Module { binder: Binder ->
                binder.bind(
                    BothNamedAnnotations::class.java
                )
            }
        )

        val bothNamedAnnotations = injector.getInstance(BothNamedAnnotations::class.java)

        assertThat(bothNamedAnnotations.stringProp1()).isEqualTo("awp")
        assertThat(bothNamedAnnotations.stringProp2()).isEqualTo("usp")
    }

    @Test
    fun `throws if deserialization fails`() {
        assertThrows<FailedToCreatePropException> {
            val injector = createInjector(
                Module { binder: Binder ->
                    binder.bind(InvalidJSON::class.java)
                }
            )

            injector.getInstance(InvalidJSON::class.java)
        }
    }

    @Test
    fun `module with no element does not throw`() {
        createInjector(Module { })
    }

    @Test
    fun `module with no binding does not throw`() {
        createInjector(Module { obj: Binder -> obj.requireExplicitBindings() })
    }

    @Test
    fun `module with provider`() {
        val injector = createInjector(
            Module { binder: Binder ->
                binder.bind(MyInterface::class.java).toProvider(MyInterfaceProvider::class.java)
            }
        )

        val myInterface = injector.getInstance(MyInterface::class.java)

        assertThat(myInterface.propValue).isEqualTo("awp")
    }

    @Test
    fun `module with empty named annotation`() {
        assertThrows<RequiredNamedAnnotationException> {
            createInjector(
                Module { binder: Binder ->
                    binder.bind(EmptyNamedAnnotation::class.java)
                }
            )
        }
    }

    @Test
    fun `private module support`() {
        val injector = createInjector(object : PrivateModule() {
            override fun configure() {
                bind(ComplexObjects::class.java)
                expose(ComplexObjects::class.java)
            }
        })

        injector.getInstance(ComplexObjects::class.java)
    }

    @Test
    fun `inline provider support`() {
        createInjector(InlineProviderModule()).getInstance(InjectedObject::class.java)
    }

    @Test
    fun `assisted inject support`() {
        val injector = createInjector(
            Module { binder: Binder ->
                binder.install(
                    FactoryModuleBuilder().build(
                        MyAssistedInjectFactoryModule::class.java
                    )
                )
            }
        )

        injector.getInstance(MyAssistedInjectFactoryModule::class.java)
    }

    @Test
    fun `assisted inject support multiple factory functions`() {
        val injector = createInjector(
            Module { binder: Binder ->
                binder.install(FactoryModuleBuilder().build(MyAssistedInjectFactoryModuleMultiple::class.java))
            }
        )

        injector.getInstance(MyAssistedInjectFactoryModuleMultiple::class.java)
    }

    private fun createInjector(vararg modules: Module): Injector {
        val autoPropModule = AutoPropModule(
            "io.pleo",
            modules.toList(),
            CommonsConfigPropFactory(),
            JacksonParserFactory(),
        )

        val allModules: List<Module> = buildList {
            add(autoPropModule)
            addAll(modules)
        }

        return try {
            Guice.createInjector(allModules)
        } catch (ex: CreationException) {
            if (ex.cause != null) {
                throw (ex.cause as RuntimeException?)!!
            }
            throw ex
        }
    }
}
