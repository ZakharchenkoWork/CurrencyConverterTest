package com.faigenbloom.currencyconvertertest.currency

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.currencyconvertertest.CurrencyArgs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CurrencyPickerViewModel(
    private val currencyRepository: CurrencyRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val args = CurrencyArgs(savedStateHandle)
    private var action: String = args.action
    private var currencies: List<String> = listOf("")
    private var chosenCurrency: String = args.currency

    private val onChosenCurrencyChanged: (String) -> Unit = {
        chosenCurrency = it
        updateUI()
    }

    private val state: CurrencyPickerState
        get() = CurrencyPickerState(
            currencies = currencies,
            chosenCurrency = chosenCurrency,
            action = action,
            onChosenCurrencyChanged = onChosenCurrencyChanged,
        )

    private val _uiStateFlow = MutableStateFlow(state)
    val uiStateFlow = _uiStateFlow.asStateFlow().apply {
        viewModelScope.launch(Dispatchers.IO) {
            currencyRepository.currencyNamesFlow.collectLatest {
                currencies = it
                updateUI()
            }
        }
    }

    private fun updateUI() = _uiStateFlow.update { state }
}

data class CurrencyPickerState(
    val currencies: List<String>,
    val chosenCurrency: String,
    val action: String,
    val onChosenCurrencyChanged: (String) -> Unit,
)

const val DEFAULT_CURRENCY = "EUR"
