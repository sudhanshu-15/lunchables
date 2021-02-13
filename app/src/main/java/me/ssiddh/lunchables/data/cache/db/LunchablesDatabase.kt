package me.ssiddh.lunchables.data.cache.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.ssiddh.lunchables.data.cache.dao.SearchResultDao
import me.ssiddh.lunchables.data.models.SearchResult
import me.ssiddh.lunchables.utils.PlaceResultTypeConverter

@Database(entities = [SearchResult::class], version = 1, exportSchema = false)
@TypeConverters(PlaceResultTypeConverter::class)
abstract class LunchablesDatabase : RoomDatabase() {
    abstract val searchResultDao: SearchResultDao
}
