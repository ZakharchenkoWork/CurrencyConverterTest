package com.faigenbloom.currencyconvertertest.currency

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.ListItemPicker
import com.faigenbloom.currencyconvertertest.R
import com.faigenbloom.currencyconvertertest.ui.theme.CurrencyConverterTestTheme

@Composable
fun CurrencyPicker(
    state: CurrencyPickerState,
    onCurrencyPicked: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(color = MaterialTheme.colorScheme.surface),
    ) {
        Text(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    top = 16.dp,
                ),
            text = stringResource(R.string.currency_dialog_title),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge,
        )
        ListItemPicker(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            value = state.chosenCurrency,
            onValueChange = state.onChosenCurrencyChanged,
            list = state.currencies
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onClick = { onCurrencyPicked(state.chosenCurrency) },
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
                    text = stringResource(R.string.dialog_button_done),
                    color = MaterialTheme.colorScheme.onSecondary,
                )
            }
        }
    }
}

@Preview
@Composable
fun CurrencyChooserPreview() {
    CurrencyConverterTestTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface,
        ) {
            CurrencyPicker(
                state = mockCurrencyPickerState,
                onCurrencyPicked = {},
            )
        }
    }
}
