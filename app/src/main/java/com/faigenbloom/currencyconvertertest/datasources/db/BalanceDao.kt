package com.faigenbloom.currencyconvertertest.datasources.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.faigenbloom.currencyconvertertest.datasources.db.entities.BalanceEntity

@Dao
interface BalanceDao {
    @Query("SELECT * FROM ${BalanceEntity.TABLE_NAME}")
    suspend fun getBalances(): List<BalanceEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(balanceEntities: List<BalanceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(balanceEntity: BalanceEntity)
}
