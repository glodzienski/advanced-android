package com.example.cities.model

import java.io.Serializable

class City(
    var id: String = "",
    var name: String = "",
    var foto: String = "",
    var country: String = ""
) : Serializable {
    override fun toString(): String = name
}