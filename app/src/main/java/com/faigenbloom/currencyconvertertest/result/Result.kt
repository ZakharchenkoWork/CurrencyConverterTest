package com.faigenbloom.currencyconvertertest.result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import com.faigenbloom.currencyconvertertest.R
import com.faigenbloom.currencyconvertertest.ui.theme.CurrencyConverterTestTheme

@Composable
fun ResultMessage(
    state: ResultState,
    onOkClicked: () -> Unit,
) {
    Box() {
        Column(
            modifier = Modifier
                .height(300.dp)
                .padding(16.dp)
                .background(color = MaterialTheme.colorScheme.surface)
                .wrapContentHeight(),
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        top = 16.dp,
                    ),
                style = MaterialTheme.typography.bodyLarge,
                text = stringResource(R.string.result_dialog_title),
                color = MaterialTheme.colorScheme.onSurface,
            )
            DialogMessage(state)
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = onOkClicked,
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
                        text = stringResource(R.string.dialog_button_done),
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
            }
        }
    }
}

@Composable
private fun DialogMessage(state: ResultState) {
    Text(
        modifier = Modifier
            .padding(
                all = 16.dp,
            ),
        text = if (state.sellFeeText.isBlank() && state.receiveFeeText.isBlank()) {
            stringResource(
                id = R.string.result_dialog_message_no_fee,
                state.sellText,
                state.receiveText,
            )
        } else if (state.sellFeeText.isNotBlank() && state.receiveFeeText.isNotBlank()) {
            stringResource(
                id = R.string.result_dialog_message_two_fees,
                state.sellText,
                state.receiveText,
                state.sellFeeText,
                state.receiveFeeText,
            )
        } else {
            stringResource(
                id = R.string.result_dialog_message,
                state.sellText,
                state.receiveText,
                if (state.sellFeeText.isNotBlank()) {
                    state.sellFeeText
                } else {
                    state.receiveFeeText
                },
            )
        },
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.bodyLarge,
    )
}

@Preview
@Composable
fun ResultMessagePreview() {
    CurrencyConverterTestTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface,
        ) {
            ResultMessage(
                state = mockResultState,
                onOkClicked = {},
            )
        }
    }
}
