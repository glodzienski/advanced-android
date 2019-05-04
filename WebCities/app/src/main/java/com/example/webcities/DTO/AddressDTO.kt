package com.example.webcities.DTO

import com.google.gson.annotations.SerializedName

data class AddressDTO (
    @SerializedName("cep") var cep: String = "",
    @SerializedName("uf") var estado: String = "",
    @SerializedName("localidade") var cidade: String = "",
    @SerializedName("bairro") var bairro: String = "",
    @SerializedName("logradouro") var logradouro: String = ""
) {
    override fun toString(): String {
        return "${logradouro}. ${bairro}, ${cidade} - ${estado}"
    }
}