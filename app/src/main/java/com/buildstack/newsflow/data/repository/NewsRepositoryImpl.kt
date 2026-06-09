package com.buildstack.newsflow.data.repository

import com.buildstack.newsflow.data.remote.NewsApiService
import com.buildstack.newsflow.domain.models.Article
import com.buildstack.newsflow.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val apiService: NewsApiService
) : NewsRepository {

    override suspend fun getTopHeadlines(page: Int, pageSize: Int): Result<List<Article>> {
        return try {
            val response = apiService.getTopHeadlines(page = page, pageSize = pageSize)
            val articles = response.articles.map { dto ->
                Article(
                    title = dto.title ?: "",
                    description = dto.description ?: "",
                    sourceName = dto.source?.name ?: "",
                    urlToImage = dto.urlToImage ?: "",
                    url = dto.url ?: "",
                    publishedAt = dto.publishedAt ?: "",
                    content = dto.content ?: ""
                )
            }.filter { it.title.isNotBlank() && it.title != "[Removed]" }
            Result.success(articles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
