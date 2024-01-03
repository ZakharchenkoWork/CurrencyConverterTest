package com.faigenbloom.currencyconvertertest.currency

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val currencyPickerModule = module {
    viewModelOf(::CurrencyPickerViewModel)
    singleOf(::CurrencyRepository)
}
