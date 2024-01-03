package com.faigenbloom.currencyconvertertest.converter

import com.faigenbloom.currencyconvertertest.common.toLongMoney
import com.faigenbloom.currencyconvertertest.datasources.db.entities.BalanceEntity
import com.faigenbloom.currencyconvertertest.datasources.network.RatesModel
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.wheneverBlocking
import java.math.BigDecimal

class PriceInteractorTest {
    private val EUR = "EUR"
    private val USD = "USD"
    private val UAH = "UAH"
    private val UAH_DOUBLE = "UAH_DOUBLE"

    val defaultBalance: List<BalanceEntity> = listOf(
        BalanceEntity(amount = 100000, currency = "EUR"),
        BalanceEntity(amount = 20000, currency = "USD"),
        BalanceEntity(amount = 15000, currency = "UAH"),
    )
    val defaultRates: RatesModel = RatesModel(
        base = EUR,
        date = "",
        rates = hashMapOf(
            EUR to BigDecimal(1.0),
            USD to BigDecimal(1.13),
            UAH to BigDecimal(31.0),
            UAH_DOUBLE to BigDecimal(31.0),
        ),
    )

    private val converterRepository: ConverterRepository = mock {
        wheneverBlocking { it.getBalances() }.thenReturn(defaultBalance)
        wheneverBlocking { it.getRates() }.thenReturn(
            RatesModel(
                base = EUR,
                date = "",
                rates = hashMapOf(
                    EUR to BigDecimal(1.0),
                    USD to BigDecimal(1.13),
                    UAH to BigDecimal(31.0),
                    UAH_DOUBLE to BigDecimal(31.0),
                ),
            ),
        )
    }

    val interactor = PriceInteractor()

    @Test
    fun `check interactor initialized`() = runTest {
        val balances = converterRepository.getBalances()
        balances shouldBe defaultBalance
        balances.first { it.currency == EUR }.amount shouldBe "1000.00".toLongMoney()
        interactor shouldNotBe null
    }

    @Test
    fun `check dont convert same currancy`() = runTest {
        interactor.execute(
            rates = defaultRates,
            balances = defaultBalance,
            amount = "1.00".toLongMoney(),
            fromCurrency = EUR,
            toCurrency = EUR,
        ) shouldBe PriceResult.SameCurrency
    }

    @Test
    fun `check euro conversion is correct`() = runTest {
        val result = interactor.execute(
            rates = defaultRates,
            balances = defaultBalance,
            amount = "100.00".toLongMoney(),
            fromCurrency = EUR,
            toCurrency = USD,
        )
        result shouldBe PriceResult.Success("113.00".toLongMoney())
    }

    @Test
    fun `check USD to EUR conversion is correct`() = runTest {
        val result = interactor.execute(
            rates = defaultRates,
            balances = defaultBalance,
            amount = "113.00".toLongMoney(),
            fromCurrency = USD,
            toCurrency = EUR,
        )
        result shouldBe PriceResult.Success("100.00".toLongMoney())
    }

    @Test
    fun `check USD to UAH conversion is correct`() = runTest {
        val result = interactor.execute(
            rates = defaultRates,
            balances = defaultBalance,
            amount = "100.00".toLongMoney(),
            fromCurrency = USD,
            toCurrency = UAH,
        )
        result shouldBe PriceResult.Success("3503.00".toLongMoney())
    }

    @Test
    fun `check inverse UAH to USD conversion is error`() = runTest {
        val result = interactor.execute(
            rates = defaultRates,
            balances = defaultBalance,
            amount = "10000.00".toLongMoney(),
            fromCurrency = USD,
            toCurrency = UAH,
            isResultForReceive = false,
        )
        result shouldBe PriceResult.NotEnoughBalance("350300.00".toLongMoney())
    }

    @Test
    fun `check UAH to USD conversion is correct`() = runTest {
        val result = interactor.execute(
            rates = defaultRates,
            balances = defaultBalance,
            amount = "70.06".toLongMoney(),
            fromCurrency = UAH,
            toCurrency = USD,
        )
        result shouldBe PriceResult.Success("2.00".toLongMoney())
    }

    @Test
    fun `check wrong first currency returns Error`() = runTest {
        val result = interactor.execute(
            rates = defaultRates,
            balances = defaultBalance,
            amount = "1.00".toLongMoney(),
            fromCurrency = "ASDF",
            toCurrency = EUR,
        )
        result shouldBe PriceResult.FirstCurrencyNotFound
    }

    @Test
    fun `check first and second have same rates`() = runTest {
        val result = interactor.execute(
            rates = defaultRates,
            balances = defaultBalance,
            amount = "3.00".toLongMoney(),
            fromCurrency = UAH,
            toCurrency = UAH_DOUBLE,
        )
        result shouldBe PriceResult.Success("3.00".toLongMoney())
    }

    @Test
    fun `check wrong second currency returns Error`() = runTest {
        val result = interactor.execute(
            rates = defaultRates,
            balances = defaultBalance,
            amount = "1.00".toLongMoney(),
            fromCurrency = EUR,
            toCurrency = "ASDF",
        )
        result shouldBe PriceResult.SecondCurrencyNotFound
    }

    @Test
    fun `check low balance returns Error`() = runTest {
        val result = interactor.execute(
            rates = defaultRates,
            balances = defaultBalance,
            amount = "99999999.99".toLongMoney(),
            fromCurrency = EUR,
            toCurrency = USD,
        )
        result shouldBe PriceResult.NotEnoughBalance("113000000.00".toLongMoney())
    }
}
