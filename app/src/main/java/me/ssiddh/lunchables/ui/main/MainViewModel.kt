package me.ssiddh.lunchables.ui.main

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import me.ssiddh.lunchables.data.models.SearchResult
import me.ssiddh.lunchables.data.repository.RestaurantSearchRepository
import me.ssiddh.lunchables.utils.SearchResultStates
import me.ssiddh.lunchables.utils.UIStates
import me.ssiddh.lunchables.utils.toLatLng

class MainViewModel(
    private val restaurantSearchRepository: RestaurantSearchRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<UIStates>(UIStates.MapUIState)

    val uiState: LiveData<UIStates>
        get() = _uiState

    private val _searchResultState = MutableLiveData<SearchResultStates>()

    val searchResultState: LiveData<SearchResultStates>
        get() = _searchResultState

    private val _locationLiveData = MutableLiveData<LatLng>()
    val locationLiveData: LiveData<LatLng>
        get() = _locationLiveData

    fun switchResultView() {
        when (_uiState.value) {
            UIStates.ListUIState -> _uiState.value = UIStates.MapUIState
            UIStates.MapUIState -> _uiState.value = UIStates.ListUIState
            else -> UIStates.MapUIState
        }
    }

    fun getNearByRestaurants(
        location: Location,
        searchQueryText: String? = null,
        type: String = "restaurant",
        radius: Int = 1500
    ) {
        _searchResultState.value = SearchResultStates.Loading
        _locationLiveData.value = location.toLatLng()
        viewModelScope.launch(
            CoroutineExceptionHandler { _, exception ->
                Log.d("SSLOG", "Faced exception while loading ${exception.message}")
                _searchResultState.value = SearchResultStates.UnknownError
            }
        ) {
            val searchResults =
                restaurantSearchRepository.searchNearByRestaurants(
                    location,
                    searchQueryText,
                    type,
                    radius
                )
            _searchResultState.value = searchResults
        }
    }

    fun toggleFavorite(searchResult: SearchResult) {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, exception ->
                Log.d("SSLOG", "Error while updating cache ${exception.message}")
            }
        ) {
            if (searchResult.isFavourite) {
                restaurantSearchRepository.removeFavoritePlace(searchResult)
            } else {
                restaurantSearchRepository.addFavoritePlace(searchResult)
            }
        }
    }

    fun permissionsDenied() {
        _uiState.value = UIStates.NoLocationUIState
    }
}
