package me.ssiddh.lunchables.di

import android.app.Application
import androidx.room.Room
import me.ssiddh.lunchables.data.cache.dao.SearchResultDao
import me.ssiddh.lunchables.data.cache.db.LunchablesDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

private const val dbName = "lunchables.database"

val databaseModule = module {
    fun provideDatabase(application: Application): LunchablesDatabase {
        return Room.databaseBuilder(
            application,
            LunchablesDatabase::class.java,
            dbName
        ).build()
    }

    fun provideSearchDao(database: LunchablesDatabase): SearchResultDao {
        return database.searchResultDao
    }

    single { provideDatabase(androidApplication()) }
    single { provideSearchDao(get()) }
}
