package com.buildstack.newsflow.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.buildstack.newsflow.domain.models.Article

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val url: String,
    val title: String,
    val description: String,
    val urlToImage: String,
    val publishedAt: String,
    val sourceName: String,
    val content: String,
    val savedAt: Long = System.currentTimeMillis()
)

fun BookmarkEntity.toArticle(): Article {
    return Article(
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        sourceName = sourceName,
        content = content
    )
}

fun Article.toBookmarkEntity(): BookmarkEntity {
    return BookmarkEntity(
        url = url,
        title = title,
        description = description,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        sourceName = sourceName,
        content = content
    )
}
