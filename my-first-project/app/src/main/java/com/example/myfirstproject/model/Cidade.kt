package com.example.myfirstproject.model

import java.io.Serializable

class Cidade(
    var id: String = "",
    var nome: String = "",
    var foto: String = "",
    var pais: String = ""
) : Serializable