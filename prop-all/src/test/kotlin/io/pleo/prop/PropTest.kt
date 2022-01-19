package io.pleo.prop

import com.google.common.truth.Truth.assertThat
import com.google.inject.Binder
import com.google.inject.CreationException
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Module
import com.google.inject.PrivateModule
import com.google.inject.assistedinject.FactoryModuleBuilder
import io.pleo.prop.archaius.ArchaiusPropFactory
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
import org.junit.Test
import org.mockito.Mockito
import javax.sql.DataSource

class PropTest {
    @Test
    fun can_read_complex_properties() {
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
        assertThat(listOfComplexObjectPropValue[0]!!.name).isEqualTo("dustII")
        assertThat(listOfComplexObjectPropValue[0]!!.age).isEqualTo(3)
        assertThat(listOfComplexObjectPropValue[1]!!.name).isEqualTo("inferno")
        assertThat(listOfComplexObjectPropValue[1]!!.age).isEqualTo(16)

        val stringPropValue = complexObjects.myStringProp()
        assertThat(stringPropValue).isEqualTo("awp")
    }

    @Test
    fun can_bind_non_prop_objects() {
        val dataSource = Mockito.mock(DataSource::class.java)
        val injector = createInjector(
            Module { binder: Binder ->
                binder.bind(DataSource::class.java).toInstance(dataSource)
                binder.bind(NoPropObject::class.java)
            }
        )

        val actual = injector.getInstance(NoPropObject::class.java)

        assertThat(actual.dataSource).isEqualTo(dataSource)
    }

    @Test(expected = FailedToCreatePropException::class)
    fun throws_on_null_values() {
        val injector = createInjector(
            Module { binder: Binder ->
                binder.bind(
                    NullValue::class.java
                )
            }
        )

        injector.getInstance(NullValue::class.java)
    }

    @Test
    fun uses_default_value_on_missing_value() {
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

    @Test(expected = RequiredNamedAnnotationException::class)
    fun throws_on_unnamed_prop() {
        val injector = createInjector(
            Module { binder: Binder ->
                binder.bind(UnnamedProp::class.java)
            }
        )

        injector.getInstance(UnnamedProp::class.java)
    }

    @Test(expected = FailedToCreatePropException::class)
    fun throws_on_invalid_default_value() {
        val injector = createInjector(
            Module { binder: Binder ->
                binder.bind(InvalidDefaultValue::class.java)
            }
        )

        injector.getInstance(InvalidDefaultValue::class.java)
    }

    @Test(expected = FailedToCreatePropException::class)
    fun throws_on_invalid_default_value_even_if_there_is_a_valid_value_in_config() {
        val injector = createInjector(
            Module { binder: Binder ->
                binder.bind(InvalidDefaultValueButValidValue::class.java)
            }
        )

        injector.getInstance(InvalidDefaultValueButValidValue::class.java)
    }

    @Test
    fun can_have_multiple_objects_using_the_same_prop() {
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
    fun can_use_twice_same_prop_in_same_object() {
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
    fun can_use_both_named_annotations() {
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

    @Test(expected = FailedToCreatePropException::class)
    fun throws_if_deserialization_fails() {
        val injector = createInjector(
            Module { binder: Binder ->
                binder.bind(InvalidJSON::class.java)
            }
        )

        injector.getInstance(InvalidJSON::class.java)
    }

    @Test
    fun module_with_no_element_does_not_throw() {
        createInjector(Module { })
    }

    @Test
    fun module_with_no_binding_does_not_throw() {
        createInjector(Module { obj: Binder -> obj.requireExplicitBindings() })
    }

    @Test
    fun module_with_provider() {
        val injector = createInjector(
            Module { binder: Binder ->
                binder.bind(MyInterface::class.java).toProvider(MyInterfaceProvider::class.java)
            }
        )

        val myInterface = injector.getInstance(MyInterface::class.java)

        assertThat(myInterface.propValue).isEqualTo("awp")
    }

    @Test(expected = RequiredNamedAnnotationException::class)
    fun module_with_empty_named_annotation() {
        createInjector(
            Module { binder: Binder ->
                binder.bind(EmptyNamedAnnotation::class.java)
            }
        )
    }

    @Test
    fun private_module_support() {
        val injector = createInjector(object : PrivateModule() {
            override fun configure() {
                bind(ComplexObjects::class.java)
                expose(ComplexObjects::class.java)
            }
        })

        injector.getInstance(ComplexObjects::class.java)
    }

    @Test
    fun inline_provider_support() {
        createInjector(InlineProviderModule()).getInstance(InjectedObject::class.java)
    }

    @Test
    fun assisted_inject_support() {
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
    fun assisted_inject_support_multiple_factory_functions() {
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
            ArchaiusPropFactory(),
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
