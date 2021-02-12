package me.ssiddh.lunchables.ui.main

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.ssiddh.lunchables.data.repository.RestaurantSearchRepository
import me.ssiddh.lunchables.network.GooglePlacesApiService
import me.ssiddh.lunchables.utils.UIStates

class MainViewModel(
    private val restaurantSearchRepository: RestaurantSearchRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<UIStates>(UIStates.MapUIState)

    val uiState: LiveData<UIStates>
        get() = _uiState

    fun switchResultView() {
        when (_uiState.value) {
            UIStates.ListUIState -> _uiState.value = UIStates.MapUIState
            UIStates.MapUIState -> _uiState.value = UIStates.ListUIState
            else -> UIStates.MapUIState
        }
    }

    fun getNearByRestaurants(location: Location, searchQueryText: String? = null) {
        viewModelScope.launch {
            val searchResults =
                restaurantSearchRepository.getNearByRestaurants(location, searchQueryText)
            Log.d("SSLOG", "Search results ${searchResults.results.size}")
        }
    }
}