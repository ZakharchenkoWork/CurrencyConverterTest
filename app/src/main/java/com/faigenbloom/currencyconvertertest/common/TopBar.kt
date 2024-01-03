package com.faigenbloom.currencyconvertertest.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faigenbloom.currencyconvertertest.R
import com.faigenbloom.currencyconvertertest.ui.theme.CurrencyConverterTestTheme

@Composable
fun TopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.secondary,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier.padding(vertical = 24.dp),
            style = MaterialTheme.typography.bodyMedium,
            text = stringResource(R.string.converter_title),
            color = MaterialTheme.colorScheme.onSecondary,
        )
    }
}

@Preview
@Composable
fun TopBarPreview() {
    CurrencyConverterTestTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column {
                TopBar()
            }
        }
    }
}
