package com.buildstack.newsflow

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.buildstack.newsflow.presentation.splash.SplashScreen
import com.buildstack.newsflow.ui.main.MainScreen

@Composable
fun MainNavigation() {
  val navController = rememberNavController()

  NavHost(navController = navController, startDestination = "splash") {
    composable("splash") {
      SplashScreen(
          onSplashFinished = {
              navController.navigate("main") {
                  popUpTo("splash") { inclusive = true }
              }
          }
      )
    }
    composable("main") {
      MainScreen(
          onArticleClick = { article ->
              val json = kotlinx.serialization.json.Json.encodeToString(com.buildstack.newsflow.domain.models.Article.serializer(), article)
              val encodedJson = java.net.URLEncoder.encode(json, "UTF-8")
              navController.navigate("article_reader/$encodedJson")
          }
      )
    }
    composable(
        route = "article_reader/{articleJson}",
        arguments = listOf(androidx.navigation.navArgument("articleJson") { type = androidx.navigation.NavType.StringType })
    ) { backStackEntry ->
        val encodedJson = backStackEntry.arguments?.getString("articleJson") ?: ""
        val decodedJson = java.net.URLDecoder.decode(encodedJson, "UTF-8")
        com.buildstack.newsflow.presentation.reader.ArticleReaderScreen(
            articleJson = decodedJson,
            onBackClick = { navController.popBackStack() }
        )
    }
  }
}
