package com.buildstack.newsflow.domain.repository

import com.buildstack.newsflow.domain.models.Article

interface NewsRepository {
    suspend fun getTopHeadlines(page: Int = 1, pageSize: Int = 20): Result<List<Article>>
}
