package com.buildstack.newsflow.domain.usecases

import com.buildstack.newsflow.domain.models.Article
import com.buildstack.newsflow.domain.repository.NewsRepository
import javax.inject.Inject

class GetTopHeadlinesUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(): Result<List<Article>> {
        return repository.getTopHeadlines()
    }
}
