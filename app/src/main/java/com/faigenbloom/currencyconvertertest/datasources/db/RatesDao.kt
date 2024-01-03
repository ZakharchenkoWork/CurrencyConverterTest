package com.faigenbloom.currencyconvertertest.datasources.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.faigenbloom.currencyconvertertest.datasources.db.entities.RateBaseEntity
import com.faigenbloom.currencyconvertertest.datasources.db.entities.RateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RatesDao {
    @Query("SELECT * FROM ${RateEntity.TABLE_NAME}")
    fun getAllFlow(): Flow<List<RateEntity>>

    @Query("SELECT * FROM ${RateBaseEntity.TABLE_NAME}")
    suspend fun getBase(): RateBaseEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rateEntities: List<RateEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBase(rateBaseEntity: RateBaseEntity)
}
