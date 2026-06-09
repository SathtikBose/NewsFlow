package com.buildstack.newsflow.domain.usecases

import com.buildstack.newsflow.domain.models.Article
import com.buildstack.newsflow.domain.repository.NewsRepository
import javax.inject.Inject

class SearchArticlesUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(query: String, page: Int = 1, pageSize: Int = 20): Result<List<Article>> {
        return repository.searchArticles(query, page, pageSize)
    }
}
