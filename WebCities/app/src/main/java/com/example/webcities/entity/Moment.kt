package com.example.webcities.entity

import java.io.Serializable

class Moment(var id: String = "",
             var nome: String = "",
             var pais: String = "",
             var imagePath: String = "",
             var address: Address = Address()
) : Serializable