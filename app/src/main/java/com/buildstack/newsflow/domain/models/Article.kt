package com.buildstack.newsflow.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val title: String,
    val description: String,
    val sourceName: String,
    val urlToImage: String,
    val url: String,
    val publishedAt: String,
    val content: String
)
