package com.example.webcities.entities

import java.io.Serializable

class City(var id: String = "",
           var nome: String = "",
           var pais: String = "",
           var imagePath: String = "") : Serializable