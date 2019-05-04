package com.example.webcities.service

import com.example.webcities.DTO.AddressDTO
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface ViaCepApiService {
    @GET("/{cep}/json")
    fun show(@Path("cep") cep: String): Observable<AddressDTO>
}