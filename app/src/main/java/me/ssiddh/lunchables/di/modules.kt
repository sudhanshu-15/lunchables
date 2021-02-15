package me.ssiddh.lunchables.di

import android.content.Context
import com.google.android.gms.location.LocationServices
import me.ssiddh.lunchables.ui.main.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val fusedLocationProviderClientModule = module {
    single { provideFusedLocationProviderClient(androidContext()) }
}

val mainViewModelModule = module {
    viewModel { MainViewModel() }
}

private fun provideFusedLocationProviderClient(context: Context) =
    LocationServices.getFusedLocationProviderClient(context)