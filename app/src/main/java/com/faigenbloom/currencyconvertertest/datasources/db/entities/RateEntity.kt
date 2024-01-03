package com.faigenbloom.currencyconvertertest.datasources.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.faigenbloom.currencyconvertertest.datasources.db.entities.RateEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class RateEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_CURRENCY)
    val currency: String,
    @ColumnInfo(name = COLUMN_VALUE)
    val value: Double,
) {
    companion object {
        const val TABLE_NAME = "rates"
        const val COLUMN_VALUE = "value"
        const val COLUMN_CURRENCY = "currency"
    }
}
