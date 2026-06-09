package com.buildstack.newsflow.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SearchHistoryEntity::class, BookmarkEntity::class], version = 2, exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {
    abstract val searchHistoryDao: SearchHistoryDao
    abstract val bookmarkDao: BookmarkDao
}
