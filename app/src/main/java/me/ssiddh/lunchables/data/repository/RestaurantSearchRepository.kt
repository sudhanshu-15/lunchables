package me.ssiddh.lunchables.data.repository

import android.location.Location
import me.ssiddh.lunchables.data.models.SearchApiResponse
import me.ssiddh.lunchables.network.GooglePlacesApiService

class RestaurantSearchRepository(
    private val apiService: GooglePlacesApiService
) {

    suspend fun getNearByRestaurants(
        location: Location,
        searchQueryText: String?,
        type: String = "restaurant",
        radius: Int = 1500
    ): SearchApiResponse {
        return apiService.getNearByPlaces(
            location = "${location.latitude},${location.longitude}",
            keyword = searchQueryText,
            type = type,
            radius = radius
        )
    }
}
