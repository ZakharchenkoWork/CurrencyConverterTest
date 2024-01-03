package com.faigenbloom.currencyconvertertest.datasources.network.usecases

import com.faigenbloom.currencyconvertertest.datasources.db.RatesDao
import com.faigenbloom.currencyconvertertest.datasources.db.entities.RateBaseEntity
import com.faigenbloom.currencyconvertertest.datasources.db.entities.RateEntity
import com.faigenbloom.currencyconvertertest.datasources.network.RatesApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class RatesBackgroundUseCase(
    private val ratesApi: RatesApi,
    private val ratesDao: RatesDao,
) {

    private var job: Job? = null

    fun startService() {
        job = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                doWork()
                delay(5000)
            }
        }
    }

    private suspend fun doWork() {
        val ratesModel = ratesApi.getRates()
        ratesDao.insertBase(
            RateBaseEntity(baseCurrency = ratesModel.base, date = ratesModel.date)
        )
        ratesDao.insertAll(
            ratesModel.rates.map {
                RateEntity(
                    currency = it.key,
                    value = it.value.toDouble(),
                )
            }
        )
    }

    fun stopService() {
        job?.cancel()
    }
}