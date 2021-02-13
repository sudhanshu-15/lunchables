package me.ssiddh.lunchables.di

import android.app.Application
import androidx.room.Room
import com.squareup.moshi.Moshi
import me.ssiddh.lunchables.data.cache.dao.SearchResultDao
import me.ssiddh.lunchables.data.cache.db.LunchablesDatabase
import me.ssiddh.lunchables.utils.PlaceResultTypeConverter
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

private const val dbName = "lunchables.database"

val databaseModule = module {
    fun provideDatabase(application: Application, moshi: Moshi): LunchablesDatabase {
        PlaceResultTypeConverter.initialize(moshi)
        return Room.databaseBuilder(
            application,
            LunchablesDatabase::class.java,
            dbName
        ).build()
    }

    fun provideSearchDao(database: LunchablesDatabase): SearchResultDao {
        return database.searchResultDao
    }

    single { provideDatabase(androidApplication(), get()) }
    single { provideSearchDao(get()) }
}
