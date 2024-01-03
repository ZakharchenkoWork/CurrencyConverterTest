package com.faigenbloom.currencyconvertertest.datasources.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.faigenbloom.currencyconvertertest.datasources.db.entities.SettingsEntity

@Dao
interface ConverterSettingsDao {
    @Query("SELECT * FROM ${SettingsEntity.TABLE_NAME}")
    suspend fun getSettings(): SettingsEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(settingsEntity: SettingsEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(settingsEntity: SettingsEntity)
}
