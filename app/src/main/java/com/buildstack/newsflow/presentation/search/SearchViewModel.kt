package com.buildstack.newsflow.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildstack.newsflow.data.local.SearchHistoryDao
import com.buildstack.newsflow.data.local.SearchHistoryEntity
import com.buildstack.newsflow.domain.models.Article
import com.buildstack.newsflow.domain.usecases.SearchArticlesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val query: String = "",
    val articles: List<Article> = emptyList(),
    val history: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSearchActive: Boolean = false
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchArticlesUseCase: SearchArticlesUseCase,
    private val searchHistoryDao: SearchHistoryDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadSearchHistory()
    }

    private fun loadSearchHistory() {
        viewModelScope.launch {
            searchHistoryDao.getSearchHistory().collectLatest { historyEntities ->
                _uiState.update { it.copy(history = historyEntities.map { entity -> entity.query }) }
            }
        }
    }

    fun onQueryChange(newQuery: String) {
        _uiState.update { it.copy(query = newQuery) }
        
        // Debounce search
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (newQuery.isBlank()) {
                _uiState.update { it.copy(articles = emptyList(), isSearchActive = false) }
                return@launch
            }
            
            _uiState.update { it.copy(isSearchActive = true) }
            delay(500) // 500ms debounce
            performSearch(newQuery)
        }
    }

    fun onSearchHistoryClick(query: String) {
        _uiState.update { it.copy(query = query, isSearchActive = true) }
        performSearch(query)
    }

    fun clearSearchHistory() {
        viewModelScope.launch {
            searchHistoryDao.clearSearchHistory()
        }
    }

    fun removeHistoryItem(query: String) {
        viewModelScope.launch {
            searchHistoryDao.deleteSearchQuery(query)
        }
    }

    private fun performSearch(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            // Save to history
            searchHistoryDao.insertSearchQuery(SearchHistoryEntity(query = query))

            searchArticlesUseCase(query = query)
                .onSuccess { articles ->
                    _uiState.update { 
                        it.copy(
                            articles = articles,
                            isLoading = false,
                            error = null
                        ) 
                    }
                }
                .onFailure { error ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = error.localizedMessage ?: "Unknown Error"
                        ) 
                    }
                }
        }
    }
}
