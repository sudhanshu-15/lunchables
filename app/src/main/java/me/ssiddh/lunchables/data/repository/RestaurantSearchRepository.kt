package me.ssiddh.lunchables.data.repository

import android.location.Location
import android.util.Log
import me.ssiddh.lunchables.data.cache.dao.SearchResultDao
import me.ssiddh.lunchables.data.models.SearchResult
import me.ssiddh.lunchables.data.models.Status
import me.ssiddh.lunchables.network.GooglePlacesApiService
import me.ssiddh.lunchables.utils.SearchResultStates
import java.io.IOException

class RestaurantSearchRepository(
    private val apiService: GooglePlacesApiService,
    private val searchResultDao: SearchResultDao
) {

    suspend fun addFavoritePlace(searchResult: SearchResult) =
        searchResultDao.insertFavPlace(searchResult)

    suspend fun removeFavoritePlace(searchResult: SearchResult) =
        searchResultDao.removeFavPlace(searchResult)

    suspend fun searchNearByRestaurants(
        location: Location,
        searchQueryText: String?,
        type: String = "restaurant",
        radius: Int = 1500
    ): SearchResultStates {
        try {
            val favoritePlaces = searchResultDao.getAllFavoritePlaces()
            Log.d("SSLOG", "FavoritePlaces ${favoritePlaces.size}")
            val favoritePlacesIds =
                favoritePlaces.map { searchResult -> searchResult.placeId }
            val apiSearchResponse = apiService.getNearByPlaces(
                location = "${location.latitude},${location.longitude}",
                keyword = searchQueryText,
                type = type,
                radius = radius
            )
            return when (apiSearchResponse.status) {
                Status.OK -> {
                    val updatedList = apiSearchResponse.results
                        .map { placeResult ->
                            SearchResult(
                                placeId = placeResult.id,
                                placeInfo = placeResult,
                                isFavourite = (favoritePlacesIds.contains(placeResult.id))
                            )
                        }
                    SearchResultStates.FoundPlaceResults(updatedList)
                }
                Status.ZERO_RESULTS -> SearchResultStates.NoResultsButCache(favoritePlaces)
                else -> SearchResultStates.ErrorWhileSearching(favoritePlaces)
            }
        } catch (e: IOException) {
            return SearchResultStates.UnknownError
        }
    }
}
