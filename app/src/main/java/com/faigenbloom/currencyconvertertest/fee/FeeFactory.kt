package com.faigenbloom.currencyconvertertest.fee

class FeeFactory(
    val list: List<FeeRule> = listOf(
        FirstFiveFreeRule()
    ),
)
