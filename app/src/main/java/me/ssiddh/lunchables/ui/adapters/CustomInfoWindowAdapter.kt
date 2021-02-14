package me.ssiddh.lunchables.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import me.ssiddh.lunchables.data.models.PlaceResult
import me.ssiddh.lunchables.databinding.CustomInfoWindowBinding

class CustomInfoWindowAdapter(private val context: Context) : GoogleMap.InfoWindowAdapter {

    private val binding: CustomInfoWindowBinding by lazy {
        return@lazy CustomInfoWindowBinding.inflate(LayoutInflater.from(context))
    }

    override fun getInfoContents(marker: Marker): View {
        renderWindow(marker, binding)
        return binding.root
    }

    override fun getInfoWindow(marker: Marker): View {
        renderWindow(marker, binding)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun renderWindow(marker: Marker, binding: CustomInfoWindowBinding) {
        val tag = marker.tag
        if (tag is PlaceResult) {
            binding.placeName.text = tag.name
            binding.placeRatingBar.rating = tag.rating.toFloat()
            val dollars = "$".repeat(tag.priceLevel ?: 0)
            val openNow = if (tag.openingHours?.openNow == true) "Yes" else "No"
            binding.placeInfo.text = "$dollars. Open Now: $openNow"
            binding.totalReviews.text = "(${tag.userRatingsNumber})"
        }
    }
}
