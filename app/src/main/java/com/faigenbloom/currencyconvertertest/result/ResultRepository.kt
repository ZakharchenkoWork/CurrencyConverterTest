package com.faigenbloom.currencyconvertertest.result

import com.faigenbloom.currencyconvertertest.datasources.db.TransactionsDao

class ResultRepository(private val transactionsDao: TransactionsDao) {
    suspend fun getResult(id: Long) = transactionsDao.getTransaction(id)
}
