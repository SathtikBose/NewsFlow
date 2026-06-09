package com.buildstack.newsflow.di

import android.app.Application
import androidx.room.Room
import com.buildstack.newsflow.data.local.NewsDatabase
import com.buildstack.newsflow.data.local.SearchHistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideNewsDatabase(app: Application): NewsDatabase {
        return Room.databaseBuilder(
            app,
            NewsDatabase::class.java,
            "news_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideSearchHistoryDao(db: NewsDatabase): SearchHistoryDao {
        return db.searchHistoryDao
    }

    @Provides
    @Singleton
    fun provideSettingsDao(db: NewsDatabase): com.buildstack.newsflow.data.local.SettingsDao {
        return db.settingsDao
    }
}
