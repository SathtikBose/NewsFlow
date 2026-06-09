package com.buildstack.newsflow

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.buildstack.newsflow.ui.main.MainScreen

@Composable
fun MainNavigation() {
  val navController = rememberNavController()

  NavHost(navController = navController, startDestination = "main") {
    composable("main") {
      MainScreen()
    }
  }
}
