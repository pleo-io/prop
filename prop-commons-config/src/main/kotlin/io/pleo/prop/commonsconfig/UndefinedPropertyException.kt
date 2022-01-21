package io.pleo.prop.commonsconfig

class UndefinedPropertyException(property: ParsingProperty<*>) :
    RuntimeException("Property '${property.name}' has no value.")
