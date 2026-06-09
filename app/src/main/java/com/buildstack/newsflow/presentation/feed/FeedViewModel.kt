package com.buildstack.newsflow.presentation.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildstack.newsflow.domain.models.Article
import com.buildstack.newsflow.domain.usecases.GetTopHeadlinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FeedUiState(
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val page: Int = 1,
    val isEndOfPaginationReached: Boolean = false
)

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    init {
        loadFeed()
    }

    fun loadFeed() {
        if (_uiState.value.isLoading || _uiState.value.isEndOfPaginationReached) return

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val currentPage = _uiState.value.page
            // We reuse top headlines for the feed, maybe shuffle them in a real app,
            // but for now, we just load them sequentially.
            val result = getTopHeadlinesUseCase(page = currentPage)
            
            result.onSuccess { newArticles ->
                _uiState.update { state ->
                    state.copy(
                        articles = state.articles + newArticles,
                        isLoading = false,
                        page = state.page + 1,
                        isEndOfPaginationReached = newArticles.isEmpty()
                    )
                }
            }.onFailure { exception ->
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        error = exception.message ?: "An unknown error occurred"
                    )
                }
            }
        }
    }

    fun refresh() {
        _uiState.update { FeedUiState() }
        loadFeed()
    }
}
