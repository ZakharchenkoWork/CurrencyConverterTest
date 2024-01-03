package com.faigenbloom.currencyconvertertest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.faigenbloom.currencyconvertertest.converter.ActionType
import com.faigenbloom.currencyconvertertest.converter.ConverterPage
import com.faigenbloom.currencyconvertertest.converter.ConverterPageViewModel
import com.faigenbloom.currencyconvertertest.currency.CurrencyPicker
import com.faigenbloom.currencyconvertertest.currency.CurrencyPickerViewModel
import com.faigenbloom.currencyconvertertest.result.ResultMessage
import com.faigenbloom.currencyconvertertest.result.ResultViewModel
import com.faigenbloom.currencyconvertertest.ui.theme.CurrencyConverterTestTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            CurrencyConverterTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Destination.Converter.route,
                    ) {
                        composable(Destination.Converter.route) { backStackEntry ->
                            ConverterScreen(
                                backStackEntry = backStackEntry,
                                onCurrencyChangeRequest = { actionType, default ->
                                    navController.navigate(
                                        Destination.CurrencyPicker.withData(
                                            action = actionType,
                                            defaultValue = default,
                                        ),
                                    )
                                },
                                onResultSaved = {
                                    navController.navigate(
                                        Destination.Result.withData(transactionId = it)
                                    )
                                },
                            )
                        }
                        dialog(
                            route = Destination.CurrencyPicker.route,
                            dialogProperties = DialogProperties(
                                dismissOnBackPress = true,
                                dismissOnClickOutside = true,
                            ),
                        ) {
                            CurrencyPickerDialog(
                                onCurrencyPicked = { action, currency ->
                                    navController.popBack(
                                        hashMapOf(
                                            ACTION_TYPE_ARG to action,
                                            VALUE_ARG to currency,
                                        ),
                                    )
                                },
                            )
                        }
                        dialog(
                            route = Destination.Result.route,
                            dialogProperties = DialogProperties(
                                dismissOnBackPress = true,
                                dismissOnClickOutside = true,
                            ),
                        ) {
                            ResultDialog(onDialogClosed = {
                                navController.popBackStack()
                            })
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ConverterScreen(
        backStackEntry: NavBackStackEntry,
        onCurrencyChangeRequest: (action: ActionType, currency: String) -> Unit,
        onResultSaved: (transactionId: Long) -> Unit,
    ) {
        val converterPageViewModel = koinViewModel<ConverterPageViewModel>()

        converterPageViewModel.handleCurrencyChange(
            backStackEntry.getPoppedArgument(ACTION_TYPE_ARG, ""),
            backStackEntry.getPoppedArgument(VALUE_ARG, ""),
        )
        converterPageViewModel.onResultSaved = onResultSaved

        val state by converterPageViewModel
            .uiStateFlow
            .collectAsState()
        ConverterPage(
            state = state,
            onCurrencyChangeRequest = onCurrencyChangeRequest,
        )
    }

    @Composable
    private fun CurrencyPickerDialog(
        onCurrencyPicked: (action: String, currency: String) -> Unit,
    ) {
        val state by koinViewModel<CurrencyPickerViewModel>()
            .uiStateFlow
            .collectAsState()

        CurrencyPicker(
            state = state,
            onCurrencyPicked = { newCurrency ->
                onCurrencyPicked(
                    state.action,
                    newCurrency,
                )
            },
        )
    }

    @Composable
    private fun ResultDialog(
        onDialogClosed: () -> Unit,
    ) {
        val state by koinViewModel<ResultViewModel>()
            .uiStateFlow
            .collectAsState()

        ResultMessage(
            state = state,
            onOkClicked = onDialogClosed,
        )
    }
}

fun NavController.popBack(data: HashMap<String, Any?>) {
    previousBackStackEntry
        ?.savedStateHandle?.apply {
            data.forEach {
                set(it.key, it.value)
            }
        }
    popBackStack()
}

@Composable
fun <T> NavBackStackEntry.getPoppedArgument(argumentKey: String, initial: T) = this.savedStateHandle
    .getStateFlow(argumentKey, initial)
    .collectAsState().value
