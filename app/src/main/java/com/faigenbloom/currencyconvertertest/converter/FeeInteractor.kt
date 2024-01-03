package com.faigenbloom.currencyconvertertest.converter

import com.faigenbloom.currencyconvertertest.datasources.db.entities.BalanceEntity
import com.faigenbloom.currencyconvertertest.datasources.db.entities.TransactionEntity
import com.faigenbloom.currencyconvertertest.fee.FeeData
import com.faigenbloom.currencyconvertertest.fee.FeeFactory

class FeeInteractor(
    private val feeFactory: FeeFactory,
) {
    fun execute(
        balances: List<BalanceEntity>,
        transactions: List<TransactionEntity>,
        sellAmount: Long,
        sellCurrency: String,
        receiveAmount: Long,
        receiveCurrency: String,
    ): FeeResult {
        val feeValue = sumFeeDataList(feeFactory.list.map {
            it.calculateFee(
                sellAmount = sellAmount,
                sellCurrency = sellCurrency,
                receiveAmount = receiveAmount,
                receiveCurrency = receiveCurrency,
                allTransactions = transactions
            )
        })
        val sellBalanceEntity = balances.first {
            it.currency == sellCurrency
        }

        return if (sellBalanceEntity.amount > sellAmount + feeValue.sellFee &&
            receiveAmount > feeValue.receiveFee
        ) {
            FeeResult.Success(feeValue)
        } else {
            FeeResult.NotEnoughBalance(feeValue)
        }
    }

    fun sumFeeDataList(feeDataList: List<FeeData>): FeeData {
        val sellFee = feeDataList.map { it.sellFee }.sum()
        val receiveFee = feeDataList.map { it.receiveFee }.sum()
        return FeeData(sellFee, receiveFee)
    }
}

sealed class FeeResult {
    data class Success(val value: FeeData) : FeeResult()
    data class NotEnoughBalance(val value: FeeData) : FeeResult()
}
