package io.pleo.prop.archaius

import com.netflix.config.Property

class UndefinedPropertyException(property: Property<*>) :
    RuntimeException("Property '${property.name}' has a no value.")
