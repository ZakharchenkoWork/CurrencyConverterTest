package com.faigenbloom.currencyconvertertest

import android.app.Application
import com.faigenbloom.currencyconvertertest.converter.converterPageModule
import com.faigenbloom.currencyconvertertest.currency.currencyPickerModule
import com.faigenbloom.currencyconvertertest.datasources.db.databaseModule
import com.faigenbloom.currencyconvertertest.datasources.network.networkModule
import com.faigenbloom.currencyconvertertest.fee.feeModule
import com.faigenbloom.currencyconvertertest.result.resultModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                databaseModule,
                networkModule,
                converterPageModule,
                currencyPickerModule,
                resultModule,
                feeModule,
            )
        }
    }
}
