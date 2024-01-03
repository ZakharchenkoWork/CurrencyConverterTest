package com.faigenbloom.currencyconvertertest.datasources.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.faigenbloom.currencyconvertertest.datasources.db.entities.SettingsEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class SettingsEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val id: Long = 1L,
    @ColumnInfo(name = COLUMN_SELL)
    val sellCurrency: String,
    @ColumnInfo(name = COLUMN_RECEIVE)
    val receiveCurrency: String,
) {
    companion object {
        const val TABLE_NAME = "settings"
        const val COLUMN_ID = "id"
        const val COLUMN_SELL = "sellCurrency"
        const val COLUMN_RECEIVE = "receiveCurrency"
    }
}
