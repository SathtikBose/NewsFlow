package com.buildstack.newsflow.domain.repository

import com.buildstack.newsflow.data.local.SettingsEntity
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSettings(): Flow<SettingsEntity?>
    suspend fun updateThemeMode(mode: String)
    suspend fun updateFontScale(scale: Float)
}
