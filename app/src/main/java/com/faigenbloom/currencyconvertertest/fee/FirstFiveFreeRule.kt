package com.faigenbloom.currencyconvertertest.fee

import com.faigenbloom.currencyconvertertest.datasources.db.entities.TransactionEntity

class FirstFiveFreeRule : FeeRule {
    override fun calculateFee(
        sellAmount: Long,
        sellCurrency: String,
        receiveAmount: Long,
        receiveCurrency: String,
        allTransactions: List<TransactionEntity>,
    ): FeeData {
        return if (allTransactions.size > 5) {
            FeeData(
                sellFee = 70L,
                receiveFee = 0L
            )
        } else {
            FeeData(
                sellFee = 0L,
                receiveFee = 0L
            )
        }
    }
}
