package com.buildstack.newsflow.presentation.reader

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildstack.newsflow.domain.models.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArticleReaderViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val articleJson: String = java.net.URLDecoder.decode(checkNotNull(savedStateHandle["articleJson"]), "UTF-8")
    val article: Article = kotlinx.serialization.json.Json.decodeFromString(Article.serializer(), articleJson)
    val url = article.url
}
