package com.faigenbloom.currencyconvertertest.datasources.network

import com.faigenbloom.currencyconvertertest.BuildConfig
import com.faigenbloom.currencyconvertertest.datasources.network.usecases.RatesBackgroundUseCase
import okhttp3.OkHttpClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    factory { provideOkHttpClient() }
    factory { provideRatesApi(get()) }
    single { provideRetrofit(get()) }
    singleOf(::RatesBackgroundUseCase)
}

private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl(BuildConfig.API_URL).client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()
}

private fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient().newBuilder().build()
}

private fun provideRatesApi(retrofit: Retrofit): RatesApi = retrofit.create(RatesApi::class.java)
