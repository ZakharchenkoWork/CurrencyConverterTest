package com.faigenbloom.currencyconvertertest.converter

import com.faigenbloom.currencyconvertertest.datasources.db.entities.BalanceEntity

fun BalanceEntity.toMoneyForUI(): BalanceItemForUI {
    return BalanceItemForUI(
        "${(this.amount.toDouble()/100.0)} ${this.currency}"
    )
}
