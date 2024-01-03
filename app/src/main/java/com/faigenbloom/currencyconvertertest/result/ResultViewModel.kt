package com.faigenbloom.currencyconvertertest.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.currencyconvertertest.ResultArgs
import com.faigenbloom.currencyconvertertest.common.toStringMoney
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResultViewModel(
    private val resultRepository: ResultRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val transactionId: Long = ResultArgs(savedStateHandle).id.toLong()
    private var sellText: String = ""
    private var receiveText: String = ""
    private var sellFeeText: String = ""
    private var receiveFeeText: String = ""

    private val state: ResultState
        get() = ResultState(
            sellText = sellText,
            receiveText = receiveText,
            sellFeeText = sellFeeText,
            receiveFeeText = receiveFeeText,
        )

    private val _uiStateFlow = MutableStateFlow(state)
    val uiStateFlow = _uiStateFlow.asStateFlow().apply {
        viewModelScope.launch(Dispatchers.IO) {
            val result = resultRepository.getResult(id = transactionId)
            sellText = "${result.sellAmount.toStringMoney()} ${result.sellCurrency}"
            receiveText = "${result.receiveAmount.toStringMoney()} ${result.receiveCurrency}"
            sellFeeText = prepareFee(result.sellFeeAmount, result.sellCurrency)
            receiveFeeText = prepareFee(result.receiveFeeAmount, result.receiveCurrency)
            updateUI()
        }
    }

    private fun prepareFee(feeAmount: Long, feeCurrency: String): String {
        return if (feeAmount > 0L) {
            "${feeAmount.toStringMoney()} ${feeCurrency}"
        } else {
            ""
        }
    }

    private fun updateUI() = _uiStateFlow.update { state }
}

data class ResultState(
    val sellText: String,
    val receiveText: String,
    val sellFeeText: String,
    val receiveFeeText: String,
)
