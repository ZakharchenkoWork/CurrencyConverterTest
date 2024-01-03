package com.faigenbloom.currencyconvertertest.datasources.db

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single {
        val database = Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "currency_converter_database",
        ).fallbackToDestructiveMigration()
            .build()
        val appDatabaseCallback = AppDatabaseCallback(database)
        appDatabaseCallback.onCreate(database.openHelper.writableDatabase)

        database
    }
    single { provideBalanceDao(get()) }
    single { provideSettingsDao(get()) }
    single { provideRatesDao(get()) }
    single { provideTransactionsDao(get()) }


}
private fun provideBalanceDao(database: AppDatabase): BalanceDao = database.balanceDao()
private fun provideSettingsDao(database: AppDatabase): ConverterSettingsDao = database.settingsDao()
private fun provideRatesDao(database: AppDatabase): RatesDao = database.ratesDao()
private fun provideTransactionsDao(database: AppDatabase): TransactionsDao = database.transactionsDao()