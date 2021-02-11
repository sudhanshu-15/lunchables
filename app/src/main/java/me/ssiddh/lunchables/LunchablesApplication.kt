package me.ssiddh.lunchables

import android.app.Application
import me.ssiddh.lunchables.di.fusedLocationProviderClientModule
import me.ssiddh.lunchables.di.mainViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class LunchablesApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@LunchablesApplication)
            modules(listOf(fusedLocationProviderClientModule, mainViewModelModule))
        }
    }
}