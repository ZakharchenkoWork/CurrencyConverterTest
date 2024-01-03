package com.faigenbloom.currencyconvertertest.currency

import com.faigenbloom.currencyconvertertest.datasources.db.RatesDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CurrencyRepository(ratesDao: RatesDao) {
    val currencyNamesFlow: Flow<List<String>> = ratesDao.getAllFlow()
        .map { rates ->
            rates.map { rate ->
                rate.currency
            }.sorted()
        }
}
