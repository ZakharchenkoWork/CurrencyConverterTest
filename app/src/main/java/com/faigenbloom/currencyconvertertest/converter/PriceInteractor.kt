package com.faigenbloom.currencyconvertertest.converter

import com.faigenbloom.currencyconvertertest.common.toDoubleMoney
import com.faigenbloom.currencyconvertertest.common.toLongMoney
import com.faigenbloom.currencyconvertertest.datasources.db.entities.BalanceEntity
import com.faigenbloom.currencyconvertertest.datasources.network.RatesModel

class PriceInteractor {
    fun execute(
        rates: RatesModel,
        balances: List<BalanceEntity>,
        amount: Long,
        fromCurrency: String,
        toCurrency: String,
        isResultForReceive: Boolean = true,
    ): PriceResult {
        if (fromCurrency == toCurrency) {
            return PriceResult.SameCurrency
        }

        val firstRate = rates.rates[fromCurrency]
            ?: return PriceResult.FirstCurrencyNotFound

        val secondRate = rates.rates[toCurrency]
            ?: return PriceResult.SecondCurrencyNotFound

        val result = if (firstRate == secondRate) {
            amount
        } else if (toCurrency == rates.base) {
            (amount.toDoubleMoney() / firstRate).toLongMoney()
        } else if (firstRate < secondRate) {
            (amount.toDoubleMoney() * firstRate * secondRate).toLongMoney()
        } else {
            (amount.toDoubleMoney() / firstRate / secondRate).toLongMoney()
        }

        var isEnoughBalance = true

        balances.firstOrNull {
            it.currency == if (isResultForReceive) {
                fromCurrency
            } else {
                toCurrency
            }
        }?.let { balance ->
            if (balance.amount < amount) {
                isEnoughBalance = false
            }
        } ?: run {
            return PriceResult.FirstCurrencyNotFound
        }

        return if (isEnoughBalance) {
            PriceResult.Success(result)
        } else {
            PriceResult.NotEnoughBalance(result)
        }
    }
}

sealed class PriceResult {
    data class Success(val value: Long) : PriceResult()
    data object SameCurrency : PriceResult()
    data object FirstCurrencyNotFound : PriceResult()
    data object SecondCurrencyNotFound : PriceResult()
    data class NotEnoughBalance(val value: Long) : PriceResult()
}
