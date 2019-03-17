package com.example.cities.adapter

import com.example.cities.model.City
import java.util.ArrayList
import java.util.HashMap

object CitiesContent {
    val ITEMS: MutableList<City> = ArrayList()

    val ITEM_MAP: MutableMap<String, City> = HashMap()

    private fun addItem(item: City) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    init {
        addItem(City(1.toString(), "Curitiba", "teste", "Brasil"))
        addItem(City(2.toString(), "São Paulo", "teste", "Brasil"))
        addItem(City(3.toString(), "Piçarras", "teste", "Brasil"))
        addItem(City(4.toString(), "Londres", "teste", "Inglaterra"))
        addItem(City(5.toString(), "Paris", "teste", "França"))
        addItem(City(6.toString(), "Orlando", "teste", "Estados Unidos"))
        addItem(City(7.toString(), "Miami", "teste", "Estados Unidos"))
        addItem(City(8.toString(), "Valfenda", "teste", "Sei la"))
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
