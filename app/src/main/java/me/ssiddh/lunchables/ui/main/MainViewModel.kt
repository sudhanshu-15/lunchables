package me.ssiddh.lunchables.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.ssiddh.lunchables.utils.UIStates

class MainViewModel : ViewModel() {

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
}