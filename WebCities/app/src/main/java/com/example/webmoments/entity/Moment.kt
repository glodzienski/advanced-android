package com.example.webmoments.entity

import java.io.Serializable

class Moment(var id: String = "",
             var nome: String = "",
             var descricao: String = "",
             var imagePath: String = "",
             var address: Address = Address()
) : Serializable