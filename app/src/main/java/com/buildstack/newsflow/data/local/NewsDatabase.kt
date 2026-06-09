package com.buildstack.newsflow.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SearchHistoryEntity::class, SettingsEntity::class], version = 4, exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {
    abstract val searchHistoryDao: SearchHistoryDao
    abstract val settingsDao: SettingsDao
}
