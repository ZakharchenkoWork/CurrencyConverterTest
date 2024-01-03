package com.faigenbloom.currencyconvertertest.datasources

import com.faigenbloom.currencyconvertertest.common.toLongMoney
import com.faigenbloom.currencyconvertertest.datasources.db.entities.BalanceEntity
import com.faigenbloom.currencyconvertertest.datasources.db.entities.SettingsEntity

val defaultBalance: List<BalanceEntity> = listOf(
    BalanceEntity(amount = "1000.00".toLongMoney(), currency = "EUR"),
)

val defaultSettings: SettingsEntity = SettingsEntity(
    sellCurrency = "EUR",
    receiveCurrency = "USD",
)
