package com.example.webcities.entities

data class City(val id: String, val name: String, val pais: String) {
    override fun toString(): String = name
}