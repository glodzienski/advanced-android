package com.example.webcities.entities

class City(val id: String, val nome: String, val pais: String) {
    override fun toString(): String = nome
}