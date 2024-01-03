package com.faigenbloom.currencyconvertertest.converter

import com.faigenbloom.currencyconvertertest.datasources.db.BalanceDao
import com.faigenbloom.currencyconvertertest.datasources.db.ConverterSettingsDao
import com.faigenbloom.currencyconvertertest.datasources.db.RatesDao
import com.faigenbloom.currencyconvertertest.datasources.db.TransactionsDao
import com.faigenbloom.currencyconvertertest.datasources.db.entities.BalanceEntity
import com.faigenbloom.currencyconvertertest.datasources.db.entities.SettingsEntity
import com.faigenbloom.currencyconvertertest.datasources.db.entities.TransactionEntity
import com.faigenbloom.currencyconvertertest.datasources.network.RatesModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import java.math.BigDecimal

class ConverterRepository(
    private val ratesDao: RatesDao,
    private val balanceDao: BalanceDao,
    private val transactionsDao: TransactionsDao,
    private val settingsDao: ConverterSettingsDao,
) {
    val ratesFlow: Flow<RatesModel> = ratesDao.getAllFlow()
        .map { rates ->
            val base = ratesDao.getBase()
            RatesModel(
                date = base.date,
                base = base.baseCurrency,
                rates = HashMap<String, BigDecimal>().apply {
                    rates.forEach { entity ->
                        put(
                            entity.currency,
                            entity.value.toBigDecimal()
                        )
                    }
                },
            )
        }

    suspend fun getBalances(): List<BalanceEntity> {
        return balanceDao.getBalances()
    }

    suspend fun updateBalances(
        sellBalanceEntity: BalanceEntity,
        receiveBalanceEntity: BalanceEntity,
    ) {
        val balanceDao = balanceDao
        balanceDao.insertOrUpdate(sellBalanceEntity)
        balanceDao.insertOrUpdate(receiveBalanceEntity)
    }

    suspend fun getSettings(): SettingsEntity {
        return settingsDao.getSettings()
    }

    suspend fun updateSettings(settingsEntity: SettingsEntity) {
        return settingsDao.update(settingsEntity)
    }

    suspend fun saveTransaction(
        sellAmount: Long,
        sellCurrency: String,
        receiveAmount: Long,
        receiveCurrency: String,
        sellFeeAmount: Long,
        receiveFeeAmount: Long,
    ): Long {
        return transactionsDao.insert(
            TransactionEntity(
                sellAmount = sellAmount,
                sellCurrency = sellCurrency,
                receiveAmount = receiveAmount,
                receiveCurrency = receiveCurrency,
                sellFeeAmount = sellFeeAmount,
                receiveFeeAmount = receiveFeeAmount,
            ),
        )
    }

    suspend fun getRates(): RatesModel {
        return ratesFlow.toList().last()
    }

    suspend fun getTransactions(): List<TransactionEntity> {
        return transactionsDao.getTransactions()
    }
}
