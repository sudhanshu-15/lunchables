package me.ssiddh.lunchables.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import me.ssiddh.lunchables.data.models.SearchResult
import me.ssiddh.lunchables.databinding.ListResultFragmentBinding
import me.ssiddh.lunchables.ui.adapters.SearchResultRVAdapter
import me.ssiddh.lunchables.utils.SearchResultStates
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ListResultFragment : Fragment() {

    companion object {
        fun newInstance() = ListResultFragment()
    }

    private var listResultFragmentBinding: ListResultFragmentBinding? = null

    private val recyclerViewAdapter: SearchResultRVAdapter by lazy {
        SearchResultRVAdapter(::toggleFavorite)
    }

    private val viewModel: MainViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding =
            ListResultFragmentBinding.inflate(inflater, container, false)
        listResultFragmentBinding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listResultFragmentBinding?.searchResultRecyclerView?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recyclerViewAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.searchResultState.observe(
            viewLifecycleOwner,
            Observer { searchResults ->
                when (searchResults) {
                    is SearchResultStates.FoundPlaceResults ->
                        recyclerViewAdapter.updateSearchResults(searchResults.searchResults)
                    is SearchResultStates.NoResultsButCache ->
                        recyclerViewAdapter.updateSearchResults(searchResults.searchResults)
                    is SearchResultStates.ErrorWhileSearching ->
                        recyclerViewAdapter.updateSearchResults(searchResults.searchResults)
                    else -> {
                        Log.d("SSLOG", "Reached an error")
                    }
                }
            }
        )
    }

    private fun toggleFavorite(searchResult: SearchResult) = viewModel.toggleFavorite(searchResult)
}
