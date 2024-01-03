package com.faigenbloom.currencyconvertertest.converter

import com.faigenbloom.currencyconvertertest.datasources.db.entities.BalanceEntity

class BalanceInteractor {
    fun execute(
        balances: List<BalanceEntity>,
        sellAmount: Long,
        sellCurrency: String,
        receiveAmount: Long,
        receiveCurrency: String,
        sellFeeAmount: Long,
        receiveFeeAmount: Long,
    ): BalancePair {
        val sellBalance = balances.first { it.currency == sellCurrency }
        val receiveBalance =
            balances.firstOrNull() { it.currency == receiveCurrency } ?: BalanceEntity(
                receiveCurrency,
                0L
            )
        val sellAmountNew = sellBalance.amount - sellAmount - sellFeeAmount
        val receiveAmountNew = receiveBalance.amount + receiveAmount - receiveFeeAmount
        return BalancePair(
            sellBalance = sellBalance.copy(amount = sellAmountNew),
            receiveBalance = receiveBalance.copy(amount = receiveAmountNew)
        )
    }
}

data class BalancePair(
    val sellBalance: BalanceEntity,
    val receiveBalance: BalanceEntity,
)