package me.ssiddh.lunchables.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.ssiddh.lunchables.R
import me.ssiddh.lunchables.data.models.SearchResult
import me.ssiddh.lunchables.databinding.CustomInfoWindowBinding

class SearchResultRVAdapter(private val clickListener: (SearchResult) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val searchResultsList = mutableListOf<SearchResult>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = CustomInfoWindowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchResultViewHolder(binding)
    }

    override fun getItemCount(): Int = searchResultsList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val searchResultViewHolder =
            holder as SearchResultViewHolder
        searchResultViewHolder.populateView(position, clickListener)
    }

    fun updateSearchResults(searchResults: List<SearchResult>) {
        searchResultsList.clear()
        searchResultsList.addAll(searchResults)
    }

    private inner class SearchResultViewHolder(
        private val viewBinding: CustomInfoWindowBinding
    ) : RecyclerView.ViewHolder(viewBinding.root) {
        fun populateView(position: Int, clickListener: (SearchResult) -> Unit) {
            val searchResult = searchResultsList[position]
            val placeResult = searchResult.placeInfo
            viewBinding.apply {
                placeName.text = placeResult.name
                placeRatingBar.rating = placeResult.rating.toFloat()
                val dollars = "$".repeat(placeResult.priceLevel ?: 0)
                val openNow = if (placeResult.openingHours?.openNow == true) "Yes" else "No"
                val placeInfoText = "$dollars. Open Now: $openNow"
                placeInfo.text = placeInfoText
                val totalReviewsText = "(${placeResult.userRatingsNumber})"
                totalReviews.text = totalReviewsText
                favButton.visibility = View.VISIBLE
                favButton.setOnClickListener {
                    clickListener(searchResult)
                    val updatedSearchResult = searchResult.copy(
                        isFavourite = !searchResult.isFavourite
                    )
                    searchResultsList[position] = updatedSearchResult
                    notifyItemChanged(position)
                }
                if (searchResult.isFavourite)
                    favButton.setImageResource(R.drawable.ic_heart_filled)
                else
                    favButton.setImageResource(R.drawable.ic_heart_hollow)
            }
        }
    }
}
