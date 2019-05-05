package com.example.webmoments.service

import com.example.webmoments.entity.Address
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface ViaCepApiService {
    @GET("{cep}/json")
    fun show(@Path("cep") cep: String): Observable<Address>
}