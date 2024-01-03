package com.faigenbloom.currencyconvertertest.datasources.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.faigenbloom.currencyconvertertest.datasources.db.entities.TransactionEntity

@Dao
interface TransactionsDao {
    @Query("SELECT * FROM ${TransactionEntity.TABLE_NAME}")
    suspend fun getTransactions(): List<TransactionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transactionEntity: TransactionEntity): Long

    @Query("SELECT * FROM ${TransactionEntity.TABLE_NAME} WHERE ${TransactionEntity.COLUMN_ID} = :id")
    suspend fun getTransaction(id: Long): TransactionEntity
}
