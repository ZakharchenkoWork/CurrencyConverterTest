package com.faigenbloom.currencyconvertertest.converter

import com.faigenbloom.currencyconvertertest.common.toLongMoney
import com.faigenbloom.currencyconvertertest.datasources.db.entities.BalanceEntity
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.kotlin.capture
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.wheneverBlocking


class BalanceInteractorTest {
    private val EUR = "EUR"
    private val USD = "USD"

    private val defaultBalance: List<BalanceEntity> = listOf(
        BalanceEntity(amount = "1000.00".toLongMoney(), currency = EUR),
    )

    private val sellBalanceCaptor: ArgumentCaptor<BalanceEntity> =
        ArgumentCaptor.forClass(BalanceEntity::class.java)
    private val receiveBalanceCaptor: ArgumentCaptor<BalanceEntity> =
        ArgumentCaptor.forClass(BalanceEntity::class.java)

    private val converterRepository: ConverterRepository = mock {
        wheneverBlocking { it.getBalances() }.thenReturn(defaultBalance)
    }

    private val interactor = BalanceInteractor()

    @Test
    fun `check interactor initialized`() = runTest {
        interactor shouldNotBe null
    }

    @Test
    fun `check interactor calculates correct sell`() = runTest {
        val balancePair = interactor.execute(
            balances = defaultBalance,
            sellAmount = "10.00".toLongMoney(),
            sellCurrency = EUR,
            receiveAmount = "30.00".toLongMoney(),
            receiveCurrency = USD,
            sellFeeAmount = "0.70".toLongMoney(),
            receiveFeeAmount = "0.0".toLongMoney()
        )
        converterRepository.updateBalances(
            sellBalanceEntity = balancePair.sellBalance,
            receiveBalanceEntity = balancePair.receiveBalance
        )
        verify(converterRepository, times(1)).updateBalances(
            sellBalanceEntity = capture(sellBalanceCaptor),
            receiveBalanceEntity = capture(receiveBalanceCaptor)
        )

        sellBalanceCaptor.value shouldBe BalanceEntity(
            currency = EUR,
            amount = "989.30".toLongMoney()
        )
        receiveBalanceCaptor.value shouldBe BalanceEntity(
            currency = USD,
            amount = "30.00".toLongMoney()
        )
    }

    @Test
    fun `check interactor calculates correct receive`() = runTest {
        val (sellBalance, receiveBalance) = interactor.execute(
            balances = defaultBalance,
            sellAmount = "10.00".toLongMoney(),
            sellCurrency = EUR,
            receiveAmount = "30.00".toLongMoney(),
            receiveCurrency = USD,
            sellFeeAmount = "0.0".toLongMoney(),
            receiveFeeAmount = "0.70".toLongMoney()
        )


        sellBalance shouldBe BalanceEntity(
            currency = EUR,
            amount = "990.00".toLongMoney()
        )
        receiveBalance shouldBe BalanceEntity(
            currency = USD,
            amount = "29.30".toLongMoney()
        )
    }
}
