package com.buildstack.newsflow.domain.repository

import com.buildstack.newsflow.domain.models.Article
import kotlinx.coroutines.flow.Flow

interface BookmarksRepository {
    fun getAllBookmarks(): Flow<List<Article>>
    fun isBookmarked(url: String): Flow<Boolean>
    suspend fun addBookmark(article: Article)
    suspend fun removeBookmark(article: Article)
    suspend fun removeBookmarkByUrl(url: String)
}
