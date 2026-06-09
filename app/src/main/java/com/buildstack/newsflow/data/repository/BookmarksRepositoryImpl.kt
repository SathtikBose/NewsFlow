package com.buildstack.newsflow.data.repository

import com.buildstack.newsflow.data.local.BookmarkDao
import com.buildstack.newsflow.data.local.toArticle
import com.buildstack.newsflow.data.local.toBookmarkEntity
import com.buildstack.newsflow.domain.models.Article
import com.buildstack.newsflow.domain.repository.BookmarksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BookmarksRepositoryImpl @Inject constructor(
    private val bookmarkDao: BookmarkDao
) : BookmarksRepository {

    override fun getAllBookmarks(): Flow<List<Article>> {
        return bookmarkDao.getAllBookmarks().map { entities ->
            entities.map { it.toArticle() }
        }
    }

    override fun isBookmarked(url: String): Flow<Boolean> {
        return bookmarkDao.isBookmarked(url)
    }

    override suspend fun addBookmark(article: Article) {
        bookmarkDao.insertBookmark(article.toBookmarkEntity())
    }

    override suspend fun removeBookmark(article: Article) {
        bookmarkDao.deleteBookmark(article.toBookmarkEntity())
    }

    override suspend fun removeBookmarkByUrl(url: String) {
        bookmarkDao.deleteBookmarkByUrl(url)
    }
}
