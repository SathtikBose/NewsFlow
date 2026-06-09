package com.buildstack.newsflow.domain.repository

import com.buildstack.newsflow.domain.models.Article

interface NewsRepository {
    suspend fun getTopHeadlines(): Result<List<Article>>
}
