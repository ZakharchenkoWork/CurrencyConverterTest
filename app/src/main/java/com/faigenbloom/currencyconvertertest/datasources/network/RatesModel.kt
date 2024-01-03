package com.faigenbloom.currencyconvertertest.datasources.network

import java.math.BigDecimal

data class RatesModel(
    val base: String,
    val date: String,
    val rates: HashMap<String, BigDecimal>,
)
