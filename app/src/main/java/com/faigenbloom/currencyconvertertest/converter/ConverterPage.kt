package com.faigenbloom.currencyconvertertest.converter

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faigenbloom.currencyconvertertest.R
import com.faigenbloom.currencyconvertertest.common.TopBar
import com.faigenbloom.currencyconvertertest.ui.theme.CurrencyConverterTestTheme

@Composable
fun ConverterPage(
    state: ConverterPageState,
    onCurrencyChangeRequest: (ActionType, String) -> Unit,
) {
    Column {
        TopBar()
        Balances(state = state)
        ExchangeBlock(
            state = state,
            onCurrencyChangeRequest = onCurrencyChangeRequest,
        )
        SubmitButton(
            onSubmit = state.onSubmit
        )
    }
}

@Composable
internal fun Balances(state: ConverterPageState) {
    Text(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        style = MaterialTheme.typography.bodySmall,
        text = stringResource(R.string.converter_balances_label),
        color = MaterialTheme.colorScheme.surfaceVariant,
    )
    LazyRow(modifier = Modifier.fillMaxWidth()) {
        items(state.balances) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodyLarge,
                text = it.quantity,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
internal fun ExchangeBlock(
    state: ConverterPageState,
    onCurrencyChangeRequest: (ActionType, String) -> Unit,
) {
    Text(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        style = MaterialTheme.typography.bodySmall,
        text = stringResource(R.string.converter_exchange_label),
        color = MaterialTheme.colorScheme.surfaceVariant,
    )
    CurrencyBlock(
        iconRes = R.drawable.icon_sell,
        actionLabelRes = R.string.converter_sell_label,
        amountText = state.sellValueText,
        chosenCurrency = state.sellCurrency,
        onAmountChanged = state.onSellValueChanged,
        isAmountError = state.isSellValueError,
        isCurrencyError = state.isSellCurrencyError,
        feeAmount = state.sellFeeValueText,
        onCurrencyChangeRequest = {
            onCurrencyChangeRequest(ActionType.SELL, state.sellCurrency)
        },
    )
    Divider(
        modifier = Modifier
            .padding(start = 68.dp, end = 16.dp)
            .padding(vertical = 8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        thickness = 2.dp,
    )
    CurrencyBlock(
        iconRes = R.drawable.icon_receive,
        actionLabelRes = R.string.converter_receive_label,
        amountText = state.receiveValueText,
        chosenCurrency = state.receiveCurrency,
        feeAmount = state.receiveFeeValueText,
        onAmountChanged = state.onReceiveValueChanged,
        isAmountError = state.isReceiveValueError,
        isCurrencyError = state.isReceiveCurrencyError,
        onCurrencyChangeRequest = {
            onCurrencyChangeRequest(ActionType.RECIEVE, state.receiveCurrency)
        },
    )
}

@Composable
internal fun CurrencyBlock(
    @DrawableRes iconRes: Int,
    @StringRes actionLabelRes: Int,
    amountText: String,
    chosenCurrency: String,
    feeAmount: String,
    isAmountError: Boolean,
    isCurrencyError: Boolean,
    onAmountChanged: (String) -> Unit,
    onCurrencyChangeRequest: () -> Unit,
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier
                    .size(size = 52.dp)
                    .padding(start = 16.dp),
                painter = painterResource(id = iconRes),
                contentDescription = null,
            )
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                style = MaterialTheme.typography.bodyMedium,
                text = stringResource(actionLabelRes),
                color = MaterialTheme.colorScheme.onSurface,
            )
            BasicTextField(
                modifier = Modifier.weight(weight = 1f),
                value = amountText,
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.End,
                    color = if (isAmountError.not()) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onError
                    },
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                onValueChange = onAmountChanged,
                decorationBox = { innerTextField ->
                    if (amountText.isEmpty()) {
                        Text(
                            modifier = Modifier,

                            text = stringResource(id = R.string.converter_amount_label),
                            textAlign = TextAlign.End,
                            color = if (isAmountError.not()) {
                                MaterialTheme.colorScheme.onSurface
                            } else {
                                MaterialTheme.colorScheme.onError
                            },
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    } else {
                        innerTextField()
                    }
                },
            )
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable { onCurrencyChangeRequest() },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier,
                    style = MaterialTheme.typography.bodyMedium,
                    text = chosenCurrency,
                    color = if (isCurrencyError.not()) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onError
                    },
                )
                Image(
                    modifier = Modifier
                        .size(size = 16.dp)
                        .padding(start = 8.dp),
                    painter = painterResource(id = R.drawable.icon_arrow),
                    contentDescription = null,
                )
            }
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 24.dp)
                .alpha(
                    if (feeAmount.isNotBlank()) {
                        1.0f
                    } else {
                        0.0f
                    }
                ),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.labelLarge,
            text = "-$feeAmount $chosenCurrency",
            color = MaterialTheme.colorScheme.onError,
        )
    }
}

@Composable
fun SubmitButton(onSubmit: () -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        onClick = onSubmit,
    ) {
        Box(
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall,
                text = stringResource(R.string.converter_submit_button),
                color = MaterialTheme.colorScheme.onSecondary,
            )
        }
    }
}

@Preview
@Composable
internal fun ConverterPagePreview() {
    CurrencyConverterTestTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface,
        ) {
            ConverterPage(
                state = mockExchangePageState,
                onCurrencyChangeRequest = { _, _ -> },
            )
        }
    }
}
