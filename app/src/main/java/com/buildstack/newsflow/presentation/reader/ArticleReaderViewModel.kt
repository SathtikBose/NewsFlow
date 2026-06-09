package com.buildstack.newsflow.presentation.reader

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildstack.newsflow.domain.models.Article
import com.buildstack.newsflow.domain.repository.BookmarksRepository
import com.buildstack.newsflow.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleReaderViewModel @Inject constructor(
    private val bookmarksRepository: BookmarksRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val articleJson: String = java.net.URLDecoder.decode(checkNotNull(savedStateHandle["articleJson"]), "UTF-8")
    val article: Article = kotlinx.serialization.json.Json.decodeFromString(Article.serializer(), articleJson)
    val url = article.url
    
    val isBookmarked: StateFlow<Boolean> = bookmarksRepository.isBookmarked(url)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun toggleBookmark(article: Article?) {
        if (article == null) return
        viewModelScope.launch {
            if (isBookmarked.value) {
                bookmarksRepository.removeBookmarkByUrl(article.url)
            } else {
                bookmarksRepository.addBookmark(article)
            }
        }
    }
}
