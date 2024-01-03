package com.faigenbloom.currencyconvertertest.converter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.currencyconvertertest.common.toLongMoney
import com.faigenbloom.currencyconvertertest.common.toStringMoney
import com.faigenbloom.currencyconvertertest.datasources.db.entities.BalanceEntity
import com.faigenbloom.currencyconvertertest.datasources.db.entities.SettingsEntity
import com.faigenbloom.currencyconvertertest.datasources.db.entities.TransactionEntity
import com.faigenbloom.currencyconvertertest.datasources.network.RatesModel
import com.faigenbloom.currencyconvertertest.datasources.network.usecases.RatesBackgroundUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConverterPageViewModel(
    private val converterRepository: ConverterRepository,
    private val priceInteractor: PriceInteractor,
    private val balanceInteractor: BalanceInteractor,
    private val feeInteractor: FeeInteractor,
    private val ratesBackgroundUseCase: RatesBackgroundUseCase,
) : ViewModel() {

    private var rates: RatesModel = RatesModel(
        base = "",
        date = "",
        rates = hashMapOf()
    )
    private var balancesEntities: List<BalanceEntity> = emptyList()
    private var transactions: List<TransactionEntity> = emptyList()
    private var balances: List<BalanceItemForUI> = emptyList()
    private var sellValueText: String = ""
    private var sellCurrency: String = ""
    private var receiveValueText: String = ""
    private var receiveCurrency: String = ""
    private var sellFeeValueText: String = ""
    private var receiveFeeValueText: String = ""
    private var isSellValueError: Boolean = false
    private var isSellCurrencyError: Boolean = false
    private var isReceiveValueError: Boolean = false
    private var isReceiveCurrencyError: Boolean = false

    fun handleCurrencyChange(action: String, currency: String) {
        if (action.isNotBlank()) {
            when (ActionType.valueOf(action)) {
                ActionType.SELL -> {
                    if (sellCurrency != currency) {
                        sellCurrency = currency
                        updateSettings()
                        updateUI()
                    }
                }

                ActionType.RECIEVE -> {
                    if (receiveCurrency != currency) {
                        receiveCurrency = currency
                        updateSettings()
                        updateUI()
                    }
                }
            }
            calculatePrice(
                amount = sellValueText,
                fromCurrency = sellCurrency,
                toCurrency = receiveCurrency,
                resultForReceive = true,
            )
        }
    }

    var onResultSaved: (transactionId: Long) -> Unit = {}

    private fun updateSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            converterRepository.updateSettings(
                SettingsEntity(
                    sellCurrency = sellCurrency,
                    receiveCurrency = receiveCurrency,
                ),
            )
        }
    }

    private val onSellValueChanged: (String) -> Unit = {
        sellValueText = it
        updateUI()
        calculatePrice(
            amount = sellValueText,
            fromCurrency = sellCurrency,
            toCurrency = receiveCurrency,
            resultForReceive = true,
        )
    }

    private val onReceiveValueChanged: (String) -> Unit = {
        receiveValueText = it
        updateUI()
        calculatePrice(
            amount = receiveValueText,
            fromCurrency = receiveCurrency,
            toCurrency = sellCurrency,
            resultForReceive = false,
        )
    }

    private val onSubmit: () -> Unit = {
        if (sellValueText.isNotBlank() && receiveValueText.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                val (sellBalance, receiveBalance) = balanceInteractor.execute(
                    balances = balancesEntities,
                    sellAmount = sellValueText.toLongMoney(),
                    sellCurrency = sellCurrency,
                    receiveAmount = receiveValueText.toLongMoney(),
                    receiveCurrency = receiveCurrency,
                    sellFeeAmount = sellFeeValueText.toLongMoney(),
                    receiveFeeAmount = receiveFeeValueText.toLongMoney(),
                )
                converterRepository.updateBalances(sellBalance, receiveBalance)

                val id = converterRepository.saveTransaction(
                    sellAmount = sellValueText.toLongMoney(),
                    sellCurrency = sellCurrency,
                    receiveAmount = receiveValueText.toLongMoney(),
                    receiveCurrency = receiveCurrency,
                    sellFeeAmount = sellFeeValueText.toLongMoney(),
                    receiveFeeAmount = receiveFeeValueText.toLongMoney(),
                )
                updateBalance()
                updateUI()
                viewModelScope.launch(Dispatchers.Main) {
                    onResultSaved(id)
                }
            }
        }
    }

    private val state: ConverterPageState
        get() = ConverterPageState(
            balances = balances,
            sellValueText = sellValueText,
            sellCurrency = sellCurrency,
            receiveValueText = receiveValueText,
            receiveCurrency = receiveCurrency,
            sellFeeValueText = sellFeeValueText,
            receiveFeeValueText = receiveFeeValueText,
            isSellValueError = isSellValueError,
            isSellCurrencyError = isSellCurrencyError,
            isReceiveValueError = isReceiveValueError,
            isReceiveCurrencyError = isReceiveCurrencyError,
            onSellValueChanged = onSellValueChanged,
            onReceiveValueChanged = onReceiveValueChanged,
            onSubmit = onSubmit,
        )

    private val _uiStateFlow = MutableStateFlow(state)
    val uiStateFlow = _uiStateFlow.asStateFlow().apply {
        viewModelScope.launch(Dispatchers.IO) {
            updateBalance()
            transactions = converterRepository.getTransactions()
            val converterSettings = converterRepository.getSettings()
            sellCurrency = converterSettings.sellCurrency
            receiveCurrency = converterSettings.receiveCurrency
            updateUI()
            converterRepository.ratesFlow.collectLatest {
                rates = it
            }
        }
    }

    private suspend fun updateBalance() {
        balancesEntities = converterRepository.getBalances()
        balances = balancesEntities.map {
            it.toMoneyForUI()
        }
    }

    private fun updateUI() = _uiStateFlow.update { state }

    private fun calculatePrice(
        amount: String,
        fromCurrency: String,
        toCurrency: String,
        resultForReceive: Boolean,
    ) {
        if (amount.isBlank()) {
            sellValueText = ""
            receiveValueText = ""
            updateUI()
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val result = priceInteractor.execute(
                rates = rates,
                balances = balancesEntities,
                amount = amount.toLongMoney(),
                fromCurrency = fromCurrency,
                toCurrency = toCurrency,
            )
            when (result) {
                is PriceResult.Success -> {
                    if (resultForReceive) {
                        receiveValueText = result.value.toStringMoney()
                    } else {
                        sellValueText = result.value.toStringMoney()
                    }
                    val feeData = feeInteractor.execute(
                        transactions = transactions,
                        balances = balancesEntities,
                        sellAmount = sellValueText.toLongMoney(),
                        sellCurrency = sellCurrency,
                        receiveAmount = receiveValueText.toLongMoney(),
                        receiveCurrency = receiveCurrency
                    )
                    when (feeData) {
                        is FeeResult.Success -> {
                            isSellValueError = false
                            isSellCurrencyError = false
                            isReceiveValueError = false
                            isReceiveCurrencyError = false
                            sellFeeValueText = feeData.value.sellFee.toStringMoney()
                            receiveFeeValueText = feeData.value.receiveFee.toStringMoney()
                        }

                        is FeeResult.NotEnoughBalance -> {
                            isSellValueError = true
                        }
                    }
                }

                is PriceResult.SameCurrency -> {
                    isSellCurrencyError = true
                    isReceiveCurrencyError = true
                }

                is PriceResult.FirstCurrencyNotFound -> {
                    if (resultForReceive) {
                        isSellCurrencyError = true
                    } else {
                        isReceiveCurrencyError = true
                    }
                }

                is PriceResult.SecondCurrencyNotFound -> {
                    if (resultForReceive) {
                        isReceiveCurrencyError = true
                    } else {
                        isSellCurrencyError = true
                    }
                }

                is PriceResult.NotEnoughBalance -> {
                    if (resultForReceive) {
                        receiveValueText = result.value.toStringMoney()
                    } else {
                        sellValueText = result.value.toStringMoney()
                    }
                    if (resultForReceive) {
                        isSellValueError = true
                    }
                }
            }
            updateUI()
        }
    }

    init {
        ratesBackgroundUseCase.startService()
    }

    override fun onCleared() {
        super.onCleared()
        ratesBackgroundUseCase.stopService()
    }
}

data class ConverterPageState(
    val balances: List<BalanceItemForUI>,
    val sellValueText: String,
    val sellCurrency: String,
    val receiveValueText: String,
    val receiveCurrency: String,
    val sellFeeValueText: String,
    val receiveFeeValueText: String,
    val isSellValueError: Boolean,
    val isSellCurrencyError: Boolean,
    val isReceiveValueError: Boolean,
    val isReceiveCurrencyError: Boolean,
    val onSellValueChanged: (String) -> Unit,
    val onReceiveValueChanged: (String) -> Unit,
    val onSubmit: () -> Unit,
)

data class BalanceItemForUI(
    val quantity: String,
)

enum class ActionType {
    SELL,
    RECIEVE,
}
