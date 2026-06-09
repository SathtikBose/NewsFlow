package com.buildstack.newsflow.di

import com.buildstack.newsflow.data.remote.NewsApiService
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://newsapi.org/"
    private const val API_KEY = "b392f2f182d04b9b9aaf72c58135fe6e"

    @Provides
    @Singleton
    fun provideOkHttpClient(@dagger.hilt.android.qualifiers.ApplicationContext context: android.content.Context): OkHttpClient {
        val authInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-Api-Key", API_KEY)
                .build()
            chain.proceed(request)
        }
        
        val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB
        val cache = okhttp3.Cache(context.cacheDir, cacheSize)

        val offlineInterceptor = Interceptor { chain ->
            var request = chain.request()
            if (!isNetworkAvailable(context)) {
                val cacheControl = okhttp3.CacheControl.Builder()
                    .maxStale(7, java.util.concurrent.TimeUnit.DAYS)
                    .build()
                request = request.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .cacheControl(cacheControl)
                    .build()
            }
            chain.proceed(request)
        }
        
        val onlineInterceptor = Interceptor { chain ->
            val response = chain.proceed(chain.request())
            val cacheControl = okhttp3.CacheControl.Builder()
                .maxAge(1, java.util.concurrent.TimeUnit.MINUTES) // Cache for 1 minute online
                .build()
            response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", cacheControl.toString())
                .build()
        }
        
        return OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(authInterceptor)
            .addInterceptor(offlineInterceptor)
            .addNetworkInterceptor(onlineInterceptor)
            .build()
    }
    
    private fun isNetworkAvailable(context: android.content.Context): Boolean {
        val connectivityManager = context.getSystemService(android.content.Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(android.net.NetworkCapabilities.TRANSPORT_WIFI) ||
               capabilities.hasTransport(android.net.NetworkCapabilities.TRANSPORT_CELLULAR) ||
               capabilities.hasTransport(android.net.NetworkCapabilities.TRANSPORT_ETHERNET)
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val json = Json { ignoreUnknownKeys = true }
        val contentType = "application/json".toMediaType()
        
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsApiService(retrofit: Retrofit): NewsApiService {
        return retrofit.create(NewsApiService::class.java)
    }
}
