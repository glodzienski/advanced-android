package com.example.webcities.entities

class City(val id: String, val name: String, val pais: String) {
    override fun toString(): String = name
}