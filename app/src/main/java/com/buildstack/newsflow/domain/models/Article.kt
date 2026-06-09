package com.buildstack.newsflow.domain.models

data class Article(
    val title: String,
    val description: String,
    val sourceName: String,
    val urlToImage: String,
    val url: String,
    val publishedAt: String,
    val content: String
)
