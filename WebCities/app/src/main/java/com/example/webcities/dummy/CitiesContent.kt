package com.example.webcities.dummy

import com.example.webcities.entity.City
import java.util.HashMap

object CitiesContent {

    val ITEMS: MutableList<City> = mutableListOf()

    val ITEM_MAP: MutableMap<String, City> = HashMap()

    fun clear() {
        ITEMS.clear()
        ITEM_MAP.clear()
    }

    fun remove(city: City) {
        ITEMS.remove(city)
        ITEM_MAP.remove(city.id)
    }
}
