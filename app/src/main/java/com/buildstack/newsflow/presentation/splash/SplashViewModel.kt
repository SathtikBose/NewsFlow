package com.buildstack.newsflow.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildstack.newsflow.domain.usecases.GetTopHeadlinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase
) : ViewModel() {

    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady.asStateFlow()

    init {
        fetchInitialData()
    }

    private fun fetchInitialData() {
        viewModelScope.launch {
            // For now, we just wait a bit and fetch the top headlines.
            // In the future, this data will be stored in a Room DB or passed to the next screen.
            getTopHeadlinesUseCase()
            // Even if it fails, we proceed to the main screen. 
            // We just want to warm up the network cache or pre-fetch data.
            _isReady.value = true
        }
    }
}
