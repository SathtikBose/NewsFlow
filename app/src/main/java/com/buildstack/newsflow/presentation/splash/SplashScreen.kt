package com.buildstack.newsflow.presentation.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val isReady by viewModel.isReady.collectAsStateWithLifecycle()
    
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.8f) }

    LaunchedEffect(Unit) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
    }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
    }

    LaunchedEffect(isReady, alpha.value) {
        if (isReady && alpha.value == 1f) {
            onSplashFinished()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "NewsFlow",
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 48.sp,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .alpha(alpha.value)
                .scale(scale.value)
        )
    }
}
