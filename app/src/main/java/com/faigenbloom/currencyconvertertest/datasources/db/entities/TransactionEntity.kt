package com.faigenbloom.currencyconvertertest.datasources.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.faigenbloom.currencyconvertertest.datasources.db.entities.TransactionEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    val id: Long = 0,
    @ColumnInfo(name = COLUMN_SELL_AMOUNT)
    val sellAmount: Long,
    @ColumnInfo(name = COLUMN_SELL_CURRENCY)
    val sellCurrency: String,
    @ColumnInfo(name = COLUMN_RECEIVE_AMOUNT)
    val receiveAmount: Long,
    @ColumnInfo(name = COLUMN_RECEIVE_CURRENCY)
    val receiveCurrency: String,
    @ColumnInfo(name = COLUMN_FEE_SELL)
    val sellFeeAmount: Long,
    @ColumnInfo(name = COLUMN_FEE_RECEIVE)
    val receiveFeeAmount: Long,
) {
    companion object {
        const val TABLE_NAME = "transactions"
        const val COLUMN_ID = "id"
        const val COLUMN_SELL_AMOUNT = "sellAmount"
        const val COLUMN_SELL_CURRENCY = "sellCurrency"
        const val COLUMN_RECEIVE_AMOUNT = "receiveAmount"
        const val COLUMN_RECEIVE_CURRENCY = "receiveCurrency"
        const val COLUMN_FEE_SELL = "sellFee"
        const val COLUMN_FEE_RECEIVE = "receiveFee"
    }
}
