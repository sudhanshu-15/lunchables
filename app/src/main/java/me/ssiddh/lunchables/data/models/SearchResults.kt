package me.ssiddh.lunchables.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResults(
    @field:Json(name = "status")
    val status: Status,
    @field:Json(name = "results")
    val results: List<PlaceResult>
)

enum class Status {
    OK,
    ZERO_RESULTS,
    OVER_QUERY_LIMIT,
    REQUEST_DENIED,
    INVALID_REQUEST,
    UNKNOWN_ERROR
}