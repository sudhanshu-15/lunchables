package me.ssiddh.lunchables.ui.main

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import me.ssiddh.lunchables.R
import me.ssiddh.lunchables.data.models.SearchResult
import me.ssiddh.lunchables.ui.adapters.CustomInfoWindowAdapter
import me.ssiddh.lunchables.utils.SearchResultStates
import me.ssiddh.lunchables.utils.toBitmap
import me.ssiddh.lunchables.utils.toLatLng
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

private const val ZOOM_LEVEL = 14F

class MapsFragment : Fragment() {

    private val viewModel: MainViewModel by sharedViewModel()

    private lateinit var map: GoogleMap

    private var markerBitmap: Bitmap? = null

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        googleMap.setInfoWindowAdapter(CustomInfoWindowAdapter(requireContext()))
        viewModel.locationLiveData.observe(
            viewLifecycleOwner,
            Observer {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(it, ZOOM_LEVEL))
            }
        )
        viewModel.searchResultState.observe(
            viewLifecycleOwner,
            Observer { searchResultState ->
                when (searchResultState) {
                    is SearchResultStates.FoundPlaceResults -> {
                        addMarkersToMap(searchResultState.searchResults)
                    }
                    is SearchResultStates.NoResultsButCache -> {
                        addMarkersToMap(searchResultState.searchResults)
                    }
                    is SearchResultStates.ErrorWhileSearching -> {
                        addMarkersToMap(searchResultState.searchResults)
                    }
                    else -> {
                        Log.d("SSLOG", "Faced an error")
                    }
                }
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("SSLOG", "onCreateView")
        val view = inflater.inflate(R.layout.fragment_maps, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        Log.d("SSLOG", "onViewCreated")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        markerBitmap = R.drawable.ic_map_marker.toBitmap(requireContext())
    }

    private fun addMarkersToMap(searchResults: List<SearchResult>) {
        map.clear()
        searchResults.forEach { searchResult ->
            val latLng = searchResult.placeInfo.geometry.toLatLng()
            val markerOptions = MarkerOptions()
                .position(latLng)
                .title(searchResult.placeInfo.name)
                .snippet("Ratings: ${searchResult.placeInfo.rating} Price:${searchResult.placeInfo.priceLevel}")
            markerBitmap?.let { markerOptions.icon(BitmapDescriptorFactory.fromBitmap(it)) }
            val marker = map.addMarker(markerOptions)
            marker.tag = searchResult.placeInfo
        }
    }

    companion object {
        fun newInstance() = MapsFragment()
    }
}
