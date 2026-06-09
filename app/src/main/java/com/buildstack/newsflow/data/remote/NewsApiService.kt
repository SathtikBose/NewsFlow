package com.buildstack.newsflow.data.remote

import com.buildstack.newsflow.data.remote.models.NewsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = "us",
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 20
    ): NewsResponseDto

}
