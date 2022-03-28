package com.ravitej.flickrsearch.ui.searchList

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.ravitej.flickrsearch.data.entities.dtos.ImageSearchDTO
import com.ravitej.flickrsearch.data.entities.dtos.ItemsDTO
import com.ravitej.flickrsearch.data.entities.dtos.Result
import com.ravitej.flickrsearch.data.repository.FlickrSearchRepository
import com.ravitej.flickrsearch.ui.searchList.SearchListFragment.Companion.RECENT_SEARCH_KEY
import com.ravitej.flickrsearch.ui.searchList.listeners.OnRecentSearchItemClickListener
import com.ravitej.flickrsearch.ui.searchList.listeners.OnSearchItemClickListener
import com.ravitej.flickrsearch.ui.searchList.util.RecentSearchConverter.convertToListFromString
import com.ravitej.flickrsearch.ui.searchList.util.RecentSearchConverter.convertToStringFromList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: FlickrSearchRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel(), OnSearchItemClickListener, OnRecentSearchItemClickListener {

    private val _searchResultFlow: MutableStateFlow<Result<ImageSearchDTO>> =
        MutableStateFlow(Result.Initial)
    val searchResultFlow: StateFlow<Result<ImageSearchDTO>> = _searchResultFlow

    private val _navigateToActionEvent: MutableSharedFlow<NavDirections> =
        MutableSharedFlow(replay = 0)
    val navigateToActionEvent: SharedFlow<NavDirections> = _navigateToActionEvent

    private val _recentSearchItems: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val recentSearchItems: StateFlow<List<String>> = _recentSearchItems

    private val _recentSearchVisibility: MutableLiveData<Boolean> = MutableLiveData()
    val recentSearchVisibility: LiveData<Boolean> = _recentSearchVisibility

    private val _recentSearchEmptyVisibility: MutableLiveData<Boolean> = MutableLiveData(false)
    val recentSearchEmptyVisibility: LiveData<Boolean> = _recentSearchEmptyVisibility

    private val _recentSearchListVisibility: MutableLiveData<Boolean> = MutableLiveData(false)
    val recentSearchListVisibility: LiveData<Boolean> = _recentSearchListVisibility

    private var _currentSearchItem = ""

    private val _recentSearchItem: MutableLiveData<String> = MutableLiveData()
    val recentSearchItem: LiveData<String> = _recentSearchItem

    init {
        sharedPreferences.getString(RECENT_SEARCH_KEY, "")?.convertToListFromString()?.let {
            _recentSearchVisibility.value = true
            _recentSearchListVisibility.value = (it.isNotEmpty())
            _recentSearchEmptyVisibility.value = (it.isNullOrEmpty())
            viewModelScope.launch {
                _recentSearchItems.emit(it)
            }
        }
    }

    fun clearSearchText() {
        _currentSearchItem = ""
        _recentSearchVisibility.value = true
        _recentSearchEmptyVisibility.value =
            recentSearchVisibility.value!! && recentSearchItems.value.isNullOrEmpty()
        _recentSearchListVisibility.value =
            recentSearchVisibility.value!! && recentSearchItems.value.isNotEmpty()
    }

    fun showSearchList() {
        _recentSearchEmptyVisibility.value = false
        _recentSearchVisibility.value = false
        _recentSearchListVisibility.value = false
    }

    fun insertRecentSearchItem(input: String) {
        viewModelScope.launch {
            sharedPreferences.getString(RECENT_SEARCH_KEY, "")?.convertToListFromString()
                ?.let { list ->
                    val tempList = mutableListOf(input)

                    if (list.size == 5) (0..3).forEach { tempList.add(list[it]) }
                    else list.forEach { tempList.add(it) }

                    _recentSearchItems.emit(tempList)
                    sharedPreferences.edit()
                        .putString(RECENT_SEARCH_KEY, tempList.convertToStringFromList()).apply()
                }
        }
    }

    fun searchImages(query: String) {
        if (_currentSearchItem != query) {
            _currentSearchItem = query
            viewModelScope.launch {
                searchRepository.getImageResults(query)
                    .collect { _searchResultFlow.emit(it) }
            }
        }
    }

    override fun onClick(item: ItemsDTO) {
        viewModelScope.launch(Dispatchers.Main) {
            val navDirections = SearchListFragmentDirections.navFromSearchListToDetail(item)
            _navigateToActionEvent.emit(navDirections)
        }
    }

    override fun onRecentSearchItemClick(item: String) {
        viewModelScope.launch {
            _recentSearchItem.value = item
            _currentSearchItem = item
            showSearchList()
            searchRepository.getImageResults(item)
                .collect { _searchResultFlow.emit(it) }
        }
    }
}