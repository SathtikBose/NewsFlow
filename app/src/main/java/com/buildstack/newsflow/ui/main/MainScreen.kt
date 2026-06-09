package com.buildstack.newsflow.ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.buildstack.newsflow.presentation.feed.FeedScreen
import com.buildstack.newsflow.presentation.headlines.TopHeadlinesScreen

@Composable
fun MainScreen(
    onArticleClick: (com.buildstack.newsflow.domain.models.Article) -> Unit
) {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Headlines", "Search", "Feed", "Bookmarks", "Settings")
    val icons = listOf(Icons.Filled.Home, Icons.Filled.Search, Icons.AutoMirrored.Filled.List, Icons.Filled.Favorite, Icons.Filled.Settings)

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        }
    ) { innerPadding ->
        // We will swap screens based on selectedItem later.
        androidx.compose.foundation.layout.Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedItem) {
                0 -> TopHeadlinesScreen(onArticleClick = onArticleClick)
                1 -> com.buildstack.newsflow.presentation.search.SearchScreen(onArticleClick = onArticleClick)
                2 -> FeedScreen(onArticleClick = onArticleClick)
                3 -> com.buildstack.newsflow.presentation.bookmarks.BookmarksScreen(onArticleClick = onArticleClick)
                else -> {
                    // Placeholders for other phases
                    androidx.compose.foundation.layout.Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        Text(text = "${items[selectedItem]} Screen (Coming Soon)")
                    }
                }
            }
        }
    }
}
