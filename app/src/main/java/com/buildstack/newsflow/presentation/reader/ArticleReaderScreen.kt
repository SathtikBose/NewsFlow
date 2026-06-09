package com.buildstack.newsflow.presentation.reader

import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.buildstack.newsflow.ui.components.glass

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ArticleReaderScreen(
    articleJson: String,
    onBackClick: () -> Unit,
    viewModel: ArticleReaderViewModel = hiltViewModel()
) {
    var progress by remember { mutableFloatStateOf(0f) }
    val url = viewModel.url

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    webViewClient = WebViewClient()
                    webChromeClient = object : WebChromeClient() {
                        override fun onProgressChanged(view: WebView?, newProgress: Int) {
                            progress = newProgress / 100f
                        }
                    }
                    loadUrl(url)
                }
            },
            update = { webView ->
                if (webView.url != url) {
                    webView.loadUrl(url)
                }
            }
        )

        if (progress < 1f) {
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Glassmorphism Back Button
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .statusBarsPadding()
                .padding(16.dp)
                .size(48.dp)
                .clip(CircleShape)
                .glass(shape = CircleShape)
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black // Assuming a light theme for the WebView content usually, but might need to adjust
            )
        }
        
    }
}
