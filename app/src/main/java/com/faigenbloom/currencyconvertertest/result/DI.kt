package com.faigenbloom.currencyconvertertest.result

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val resultModule = module {
    viewModelOf(::ResultViewModel)
    singleOf(::ResultRepository)
}
