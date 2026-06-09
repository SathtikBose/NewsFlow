package com.buildstack.newsflow.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey
    val id: Int = 1,
    val themeMode: String = "SYSTEM", // "LIGHT", "DARK", "SYSTEM"
    val fontScale: Float = 1.0f
)
