package com.buildstack.newsflow.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildstack.newsflow.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val themeMode: String = "SYSTEM",
    val fontScale: Float = 1.0f
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.getSettings().collectLatest { settings ->
                if (settings != null) {
                    _uiState.update { 
                        it.copy(
                            themeMode = settings.themeMode,
                            fontScale = settings.fontScale
                        )
                    }
                }
            }
        }
    }

    fun setThemeMode(mode: String) {
        viewModelScope.launch {
            settingsRepository.updateThemeMode(mode)
        }
    }

    fun setFontScale(scale: Float) {
        viewModelScope.launch {
            settingsRepository.updateFontScale(scale)
        }
    }
}
