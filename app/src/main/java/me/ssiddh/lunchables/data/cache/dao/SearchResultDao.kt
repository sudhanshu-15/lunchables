package me.ssiddh.lunchables.data.cache.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.ssiddh.lunchables.data.models.SearchResult

@Dao
interface SearchResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavPlace(searchResult: SearchResult)

    @Delete
    suspend fun removeFavPlace(searchResult: SearchResult)

    @Query("SELECT * FROM favorite_places")
    suspend fun getAllFavoritePlaces(): List<SearchResult>
}
