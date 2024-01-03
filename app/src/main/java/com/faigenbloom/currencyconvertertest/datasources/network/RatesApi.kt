package com.faigenbloom.currencyconvertertest.datasources.network

import retrofit2.http.GET

interface RatesApi {
    @GET("currency-exchange-rates")
    suspend fun getRates(): RatesModel
}
