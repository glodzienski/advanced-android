package com.example.webcities.service

import com.example.webcities.DTO.AddressDTO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ViaCepService {
    private lateinit var compositeDisposable: CompositeDisposable

    private val BASE_URL = "https://viacep.com.br/ws/{CEP}/json/"

    private lateinit var response: AddressDTO

    private fun load(cep: String) {
        val requestInterface = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(ViaCepApiService::class.java)

        compositeDisposable.add(
            requestInterface.show(cep)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        )
    }

    private fun handleResponse(addressDTO: AddressDTO) {
        response = addressDTO
    }

    private fun handleError(error: Throwable) {
        response = AddressDTO()
    }

    fun consult(cep: String): ViaCepService {
        if (::response.isInitialized) {
            return this
        }

        compositeDisposable = CompositeDisposable()
        load(cep)

        return this
    }

    fun getResponse(): AddressDTO {
        return response
    }
}