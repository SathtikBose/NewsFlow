package com.buildstack.newsflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.buildstack.newsflow.theme.NewsFlowTheme

import dagger.hilt.android.AndroidEntryPoint

import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  private val viewModel: MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    enableEdgeToEdge()
    setContent {
      val settings by viewModel.settings.collectAsStateWithLifecycle()
      
      val isDarkTheme = when (settings?.themeMode) {
          "LIGHT" -> false
          "DARK" -> true
          else -> isSystemInDarkTheme()
      }
      
      val fontScale = settings?.fontScale ?: 1.0f

      NewsFlowTheme(darkTheme = isDarkTheme, fontScale = fontScale) { 
          Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) { 
              MainNavigation() 
          } 
      }
    }
  }
}
