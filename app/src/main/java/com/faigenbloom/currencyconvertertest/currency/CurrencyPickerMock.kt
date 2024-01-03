package com.faigenbloom.currencyconvertertest.currency

val mockCurrencyPickerState = CurrencyPickerState(
    currencies = listOf(
        "EUR",
        "USD",
        "UAH",
        "ZMK",
        "MUR",
        "AED",
        "BIF",
        "BND",
        "BZD",
        "CAD",
        "DJF",
    ),
    action = "",
    chosenCurrency = "EUR",
    onChosenCurrencyChanged = {},
)
