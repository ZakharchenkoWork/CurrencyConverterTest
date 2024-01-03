package com.faigenbloom.currencyconvertertest.datasources.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.faigenbloom.currencyconvertertest.datasources.db.entities.BalanceEntity
import com.faigenbloom.currencyconvertertest.datasources.db.entities.RateBaseEntity
import com.faigenbloom.currencyconvertertest.datasources.db.entities.RateEntity
import com.faigenbloom.currencyconvertertest.datasources.db.entities.SettingsEntity
import com.faigenbloom.currencyconvertertest.datasources.db.entities.TransactionEntity
import com.faigenbloom.currencyconvertertest.datasources.defaultBalance
import com.faigenbloom.currencyconvertertest.datasources.defaultSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        BalanceEntity::class,
        SettingsEntity::class,
        TransactionEntity::class,
        RateEntity::class,
        RateBaseEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun balanceDao(): BalanceDao
    abstract fun settingsDao(): ConverterSettingsDao
    abstract fun transactionsDao(): TransactionsDao
    abstract fun ratesDao(): RatesDao
}

class AppDatabaseCallback(private val appDatabase: AppDatabase) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        CoroutineScope(Dispatchers.IO).launch {
            appDatabase.balanceDao().insertAll(defaultBalance)
            appDatabase.settingsDao().insert(defaultSettings)
        }
    }
}
