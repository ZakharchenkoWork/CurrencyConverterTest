package com.faigenbloom.currencyconvertertest

import androidx.lifecycle.SavedStateHandle
import com.faigenbloom.currencyconvertertest.converter.ActionType
import com.faigenbloom.currencyconvertertest.currency.DEFAULT_CURRENCY

sealed class Destination(val route: String) {
    object Converter : Destination(route = "Converter")

    object CurrencyPicker :
        Destination(route = "CurrencyPicker/$ACTION_TYPE_PLACEHOLDER/$VALUE_PLACEHOLDER") {
        fun withData(action: ActionType, defaultValue: String): String {
            return route
                .replace(ACTION_TYPE_PLACEHOLDER, action.name)
                .replace(VALUE_PLACEHOLDER, defaultValue)
        }
    }

    object Result : Destination(route = "Result/$VALUE_PLACEHOLDER") {
        fun withData(transactionId: Long): String {
            return route
                .replace(VALUE_PLACEHOLDER, "$transactionId")
        }
    }

}

internal class CurrencyArgs(
    val action: String,
    val currency: String,
) {
    constructor(savedStateHandle: SavedStateHandle) :
        this(
            action = savedStateHandle[ACTION_TYPE_ARG] ?: "",
            currency = savedStateHandle[VALUE_ARG] ?: DEFAULT_CURRENCY,
        )
}
internal class ResultArgs(
    val id: String,
) {
    constructor(savedStateHandle: SavedStateHandle) :
        this(
            id = savedStateHandle[VALUE_ARG] ?: "",
        )
}

const val ACTION_TYPE_ARG: String = "ACTION_TYPE_ARG"
const val ACTION_TYPE_PLACEHOLDER: String = "{$ACTION_TYPE_ARG}"

const val VALUE_ARG: String = "VALUE_ARG"
const val VALUE_PLACEHOLDER: String = "{$VALUE_ARG}"
