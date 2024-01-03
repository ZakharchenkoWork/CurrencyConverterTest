package com.faigenbloom.currencyconvertertest.converter

import com.faigenbloom.currencyconvertertest.common.toLongMoney
import com.faigenbloom.currencyconvertertest.datasources.db.entities.BalanceEntity
import com.faigenbloom.currencyconvertertest.datasources.db.entities.TransactionEntity
import com.faigenbloom.currencyconvertertest.fee.FeeData
import com.faigenbloom.currencyconvertertest.fee.FeeFactory
import com.faigenbloom.currencyconvertertest.fee.FirstFiveFreeRule
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

class FeeInteractorTest {
    private val EUR = "EUR"
    private val USD = "USD"
    private val defaultTransaction = TransactionEntity(
        sellAmount = 0L,
        sellCurrency = EUR,
        receiveAmount = 0L,
        receiveCurrency = USD,
        sellFeeAmount = "0.70".toLongMoney(),
        receiveFeeAmount = "0.0".toLongMoney()
    )
    private val defaultBalance: List<BalanceEntity> = listOf(
        BalanceEntity(amount = "1000.00".toLongMoney(), currency = EUR),
    )

    private val interactor = FeeInteractor(
        FeeFactory(
            list = listOf(
                FirstFiveFreeRule()
            ),
        )
    )

    @Test
    fun `check interactor initialized`() = runTest {
        interactor shouldNotBe null
    }

    @Test
    fun `check first five is free`() = runTest {
        val result = interactor.execute(
            balances = defaultBalance,
            transactions = prepareTransactions(4),
            sellAmount = "500.00".toLongMoney(),
            sellCurrency = EUR,
            receiveAmount = "50.00".toLongMoney(),
            receiveCurrency = USD
        )
        result shouldBe FeeResult.Success(FeeData(0L, 0L))
    }

    @Test
    fun `check after five there is fee`() = runTest {
        val result = interactor.execute(
            balances = defaultBalance,
            transactions = prepareTransactions(8),
            sellAmount = "500.00".toLongMoney(),
            sellCurrency = EUR,
            receiveAmount = "50.00".toLongMoney(),
            receiveCurrency = USD
        )
        result shouldBe FeeResult.Success(FeeData("0.70".toLongMoney(), "0.00".toLongMoney()))
    }

    @Test
    fun `check not enough balance fails`() = runTest {
        val result = interactor.execute(
            balances = defaultBalance,
            transactions = prepareTransactions(8),
            sellAmount = "1500.00".toLongMoney(),
            sellCurrency = EUR,
            receiveAmount = "50.00".toLongMoney(),
            receiveCurrency = USD
        )
        result shouldBe FeeResult.NotEnoughBalance(
            FeeData(
                "0.70".toLongMoney(),
                "0.00".toLongMoney()
            )
        )
    }

    private fun prepareTransactions(count: Int): List<TransactionEntity> {
        return ArrayList<TransactionEntity>().apply {
            for (i in 0..count) {
                add(defaultTransaction)
            }
        }
    }
}