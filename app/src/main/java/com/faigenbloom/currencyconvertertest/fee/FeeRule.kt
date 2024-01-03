package com.faigenbloom.currencyconvertertest.fee

import com.faigenbloom.currencyconvertertest.datasources.db.entities.TransactionEntity

interface FeeRule {
    fun calculateFee(
        sellAmount: Long,
        sellCurrency: String,
        receiveAmount: Long,
        receiveCurrency: String,
        allTransactions: List<TransactionEntity>,
    ): FeeData
}
