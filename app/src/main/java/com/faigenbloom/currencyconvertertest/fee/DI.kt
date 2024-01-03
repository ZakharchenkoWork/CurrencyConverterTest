package com.faigenbloom.currencyconvertertest.fee

import org.koin.dsl.module

val feeModule = module {
    single { provideFeeFactory() }
}

private fun provideFeeFactory(): FeeFactory {
    return FeeFactory()
}