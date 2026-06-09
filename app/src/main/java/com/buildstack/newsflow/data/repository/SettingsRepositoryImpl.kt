package com.buildstack.newsflow.data.repository

import com.buildstack.newsflow.data.local.SettingsDao
import com.buildstack.newsflow.data.local.SettingsEntity
import com.buildstack.newsflow.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val settingsDao: SettingsDao
) : SettingsRepository {

    override fun getSettings(): Flow<SettingsEntity?> {
        return settingsDao.getSettings()
    }

    override suspend fun updateThemeMode(mode: String) {
        val currentSettings = settingsDao.getSettings().firstOrNull() ?: SettingsEntity()
        settingsDao.insertSettings(currentSettings.copy(themeMode = mode))
    }

    override suspend fun updateFontScale(scale: Float) {
        val currentSettings = settingsDao.getSettings().firstOrNull() ?: SettingsEntity()
        settingsDao.insertSettings(currentSettings.copy(fontScale = scale))
    }
}
