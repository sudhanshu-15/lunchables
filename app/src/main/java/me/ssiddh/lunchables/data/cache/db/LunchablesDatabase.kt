package me.ssiddh.lunchables.data.cache.db

import androidx.room.Database
import androidx.room.RoomDatabase
import me.ssiddh.lunchables.data.cache.dao.SearchResultDao
import me.ssiddh.lunchables.data.models.SearchResult

@Database(entities = [SearchResult::class], version = 1)
abstract class LunchablesDatabase : RoomDatabase() {
    abstract val searchResultDao: SearchResultDao
}
