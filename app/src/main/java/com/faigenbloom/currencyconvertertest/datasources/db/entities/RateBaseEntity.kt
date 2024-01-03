package com.faigenbloom.currencyconvertertest.datasources.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.faigenbloom.currencyconvertertest.datasources.db.entities.RateBaseEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class RateBaseEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val id: Long = 1,
    @ColumnInfo(name = COLUMN_CURRENCY)
    val baseCurrency: String,
    @ColumnInfo(name = COLUMN_DATE)
    val date: String,
) {
    companion object {
        const val TABLE_NAME = "rateBase"
        const val COLUMN_ID = "id"
        const val COLUMN_CURRENCY = "baseCurrency"
        const val COLUMN_DATE = "date"
    }
}