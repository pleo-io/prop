package io.pleo.prop.objects

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

@Suppress("unused", "unused_parameter")
class InjectedObjectWithConstructor
    @JsonCreator
    constructor(
        @JsonProperty("name") var name: String,
        @JsonProperty("age") age: Int,
    ) {
        var age = 0
    }
