package com.mataku.scrobscrob.app.ui.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.mataku.scrobscrob.app.ui.molecule.SunsetBottomNavigation
import com.mataku.scrobscrob.app.ui.navigation.NavigationGraph

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen() {
    val navController = rememberAnimatedNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Scaffold(
        bottomBar = {
            if (currentRoute?.startsWith("webview?url") == false) {
                SunsetBottomNavigation(navController = navController)
            }
        }
    ) {
        NavigationGraph(navController)
    }
}