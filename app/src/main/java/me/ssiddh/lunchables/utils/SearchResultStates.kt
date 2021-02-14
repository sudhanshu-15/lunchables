package me.ssiddh.lunchables.utils

import me.ssiddh.lunchables.data.models.SearchResult

sealed class SearchResultStates {
    data class FoundPlaceResults(val searchResults: List<SearchResult>) : SearchResultStates()
    data class NoResultsButCache(val searchResults: List<SearchResult>) : SearchResultStates()
    data class ErrorWhileSearching(val searchResults: List<SearchResult>) : SearchResultStates()
    object UnknownError : SearchResultStates()
    object Loading : SearchResultStates()
}
