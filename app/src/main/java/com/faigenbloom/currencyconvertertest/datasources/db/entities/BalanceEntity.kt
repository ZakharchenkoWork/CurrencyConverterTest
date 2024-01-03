package com.faigenbloom.currencyconvertertest.datasources.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.faigenbloom.currencyconvertertest.datasources.db.entities.BalanceEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class BalanceEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_CURRENCY)
    val currency: String,
    @ColumnInfo(name = COLUMN_AMOUNT)
    val amount: Long,
) {
    companion object {
        const val TABLE_NAME = "budget"
        const val COLUMN_AMOUNT = "amount"
        const val COLUMN_CURRENCY = "currency"
    }
}
