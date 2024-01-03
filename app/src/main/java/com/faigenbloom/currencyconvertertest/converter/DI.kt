package com.faigenbloom.currencyconvertertest.converter

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val converterPageModule = module {
    viewModelOf(::ConverterPageViewModel)
    singleOf(::ConverterRepository)
    singleOf(::PriceInteractor)
    singleOf(::BalanceInteractor)
    singleOf(::FeeInteractor)
}
