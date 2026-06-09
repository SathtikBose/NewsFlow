package com.buildstack.newsflow.presentation.headlines

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.buildstack.newsflow.domain.models.Article
import com.buildstack.newsflow.ui.components.glass

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopHeadlinesScreen(
    refreshCounter: Int = 0,
    viewModel: TopHeadlinesViewModel = hiltViewModel(),
    onArticleClick: (com.buildstack.newsflow.domain.models.Article) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    // Pagination logic
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastIndex ->
                if (lastIndex != null && lastIndex >= uiState.articles.size - 5 && !uiState.isLoading) {
                    viewModel.loadHeadlines()
                }
            }
    }

    LaunchedEffect(refreshCounter) {
        if (refreshCounter > 0) {
            viewModel.refresh()
            listState.animateScrollToItem(0)
        }
    }

    PullToRefreshBox(
        isRefreshing = uiState.isLoading && uiState.articles.isNotEmpty(),
        onRefresh = { viewModel.refresh() },
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Text(
                    text = "Top Headlines",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.padding(bottom = 8.dp, top = 24.dp)
                )
            }

            itemsIndexed(uiState.articles) { index, article ->
                ArticleGlassCard(article = article, onClick = { onArticleClick(article) })
            }

            if (uiState.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            
            if (uiState.error != null && uiState.articles.isEmpty()) {
                item {
                    Text(
                        text = uiState.error ?: "Error loading articles",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ArticleGlassCard(article: Article, onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
            .clip(MaterialTheme.shapes.large)
            .background(Color.DarkGray)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = article.urlToImage,
            contentDescription = article.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        
        // Glass Overlay at the bottom
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(12.dp)
                .fillMaxWidth()
                .glass()
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = article.sourceName,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = Color.White.copy(alpha = 0.8f),
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 22.sp
                    ),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
