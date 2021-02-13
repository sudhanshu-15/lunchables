package me.ssiddh.lunchables.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_places")
data class SearchResult(
    @PrimaryKey val placeId: String,
    @ColumnInfo(name = "place_info") val placeInfo: PlaceResult,
    @ColumnInfo(name = "is_fav") val isFavourite: Boolean = false
)
