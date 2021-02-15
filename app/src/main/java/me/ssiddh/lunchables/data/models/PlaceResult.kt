package me.ssiddh.lunchables.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlaceResult(
    @field:Json(name = "business_status")
    val businessStatus: BusinessStatus?,

    @field:Json(name = "icon")
    val iconUrl: String,

    @field:Json(name = "geometry")
    val geometry: Geometry,

    @field:Json(name = "name")
    val name: String,

    @field:Json(name = "opening_hours")
    val openingHours: OpeningHours?,

    @field:Json(name = "place_id")
    val id: String,

    @field:Json(name = "price_level")
    val priceLevel: Int?,

    @field:Json(name = "rating")
    val rating: Double,

    @field:Json(name = "types")
    val types: List<String>,

    @field:Json(name = "vicinity")
    val address: String,

    @field:Json(name = "user_ratings_total")
    val userRatingsNumber: Int?
)

enum class BusinessStatus {
    @Json(name = "OPERATIONAL")
    OPERATIONAL,
    @Json(name = "CLOSED_TEMPORARILY")
    CLOSEDTEMPORARILY,
    @Json(name = "CLOSED_PERMANENTLY")
    CLOSEDPERMANENTLY
}

@JsonClass(generateAdapter = true)
data class Geometry(@field:Json(name = "location") val location: Location)

@JsonClass(generateAdapter = true)
data class Location(
    @field:Json(name = "lat")
    val lat: Double,

    @field:Json(name = "lng")
    val lng: Double
)

@JsonClass(generateAdapter = true)
data class OpeningHours(@field:Json(name = "open_now")val openNow: Boolean?)