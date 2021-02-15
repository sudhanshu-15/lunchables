package me.ssiddh.lunchables.utils

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import me.ssiddh.lunchables.data.models.PlaceResult

class PlaceResultTypeConverter {

    companion object {
        private lateinit var moshi: Moshi
        fun initialize(moshi: Moshi) {
            this.moshi = moshi
        }
    }

    @TypeConverter
    fun fromString(value: String): PlaceResult? {
        val adapter: JsonAdapter<PlaceResult> = moshi.adapter(PlaceResult::class.java)
        return adapter.fromJson(value)
    }

    @TypeConverter
    fun fromPlaceResult(placeResult: PlaceResult): String {
        val adapter: JsonAdapter<PlaceResult> = moshi.adapter(PlaceResult::class.java)
        return adapter.toJson(placeResult)
    }
}
