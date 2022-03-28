package com.ravitej.flickrsearch.ui.searchList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ravitej.flickrsearch.R
import com.ravitej.flickrsearch.data.entities.dtos.ImageSearchDTO
import com.ravitej.flickrsearch.data.entities.dtos.Result
import com.ravitej.flickrsearch.databinding.FragmentSearchListBinding
import com.ravitej.flickrsearch.ui.searchList.adapter.RecentSearchesAdapter
import com.ravitej.flickrsearch.ui.searchList.adapter.SearchListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchListFragment : Fragment() {

    private lateinit var binding: FragmentSearchListBinding
    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var adapter: SearchListAdapter
    private lateinit var recentSearchAdapter: RecentSearchesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_list, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = searchViewModel
        init()
        return binding.root
    }

    private fun init() {
        initAdapters()
        initObservers()
    }

    private fun initAdapters() {
        recentSearchAdapter = RecentSearchesAdapter(searchViewModel)
        binding.rvRecentSearchList.adapter = recentSearchAdapter

        adapter = SearchListAdapter(searchViewModel)
        binding.rvImageList.adapter = adapter
    }

    private fun initObservers() {

        searchViewModel.recentSearchItem.observe(viewLifecycleOwner) { value ->
            value?.let { binding.searchEditText.setText(it) }
        }

        searchViewModel.recentSearchEmptyVisibility.observe(viewLifecycleOwner) { value ->
            value?.let {
                binding.tvNoRecentSearches.visibility = if (it) View.VISIBLE else View.GONE
            }
        }

        searchViewModel.recentSearchListVisibility.observe(viewLifecycleOwner) { value ->
            value?.let {
                binding.rvRecentSearchList.visibility = if (it) View.VISIBLE else View.GONE
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            searchViewModel.recentSearchItems.collect {
                recentSearchAdapter.submitList(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            searchViewModel.searchResultFlow.collect {
                when (it) {
                    is Result.Success<ImageSearchDTO> -> {
                        binding.loadingIndicator.visibility = View.GONE
                        adapter.submitList(it.data.items)
                    }
                    is Result.Error -> {
                        binding.loadingIndicator.visibility = View.GONE
                    }
                    is Result.Loading -> {
                        binding.loadingIndicator.visibility = View.VISIBLE
                    }
                    is Result.Initial -> {}
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            searchViewModel.navigateToActionEvent.collect {
                findNavController().navigate(it)
            }
        }

        binding.searchBtn.setOnClickListener {
            if (!binding.searchEditText.text.isNullOrEmpty()) {
                searchViewModel.insertRecentSearchItem(binding.searchEditText.text.toString())
            }
        }

        binding.searchEditText.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty()) {
                searchViewModel.clearSearchText()
            } else {
                searchViewModel.showSearchList()
                searchViewModel.searchImages(text.toString())
            }
        }
    }

    companion object {
        const val RECENT_SEARCH_KEY = "recent_searches"
    }
}