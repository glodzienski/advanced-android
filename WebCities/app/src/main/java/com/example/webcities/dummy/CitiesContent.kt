package com.example.webcities.dummy

import com.example.webcities.entities.City
import java.util.ArrayList
import java.util.HashMap

object CitiesContent {

    /**
     * An array of sample (dummy) items.
     */
    val ITEMS: MutableList<City> = ArrayList()

    /**
     * A map of sample (dummy) items, by ID.
     */
    val ITEM_MAP: MutableMap<String, City> = HashMap()

    private val COUNT = 25

    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            add(createCity(i))
        }
    }

    fun add(item: City) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    fun remove(item: City) {
        ITEMS.remove(item)
        ITEM_MAP.remove(item.id)
    }

    private fun createCity(position: Int): City {
        return City(position.toString(), "Cidade" + position, makeDetails(position))
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0..position - 1) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }
}
