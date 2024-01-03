package com.faigenbloom.currencyconvertertest.converter

val mockExchangePageState: ConverterPageState = ConverterPageState(
    balances = listOf(
        BalanceItemForUI(quantity = "1000.00 EUR"),
        BalanceItemForUI(quantity = "0.00 USD"),
    ),
    sellValueText = "100.00",
    sellCurrency = "EUR",
    receiveValueText = "+110.30",
    receiveCurrency = "USD",
    sellFeeValueText = "100.00",
    receiveFeeValueText = "",
    isSellValueError = false,
    isSellCurrencyError = false,
    isReceiveValueError = false,
    isReceiveCurrencyError = false,
    onSellValueChanged = {},
    onReceiveValueChanged = {},
    onSubmit = {},
)
