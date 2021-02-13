package me.ssiddh.lunchables.network

import me.ssiddh.lunchables.data.models.SearchApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlacesApiService {

    @GET("nearbysearch/json")
    suspend fun getNearByPlaces(
        @Query("location") location: String,
        @Query("radius") radius: Int = 1500,
        @Query("type") type: String = "restaurant",
        @Query("keyword") keyword: String?
    ): SearchApiResponse
}
