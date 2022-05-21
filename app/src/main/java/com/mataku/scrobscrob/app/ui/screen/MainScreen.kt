package com.mataku.scrobscrob.app.ui.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mataku.scrobscrob.app.ui.molecule.SunsetBottomNavigation
import com.mataku.scrobscrob.app.ui.navigation.NavigationGraph
import com.mataku.scrobscrob.ui_common.SunsetBottomNavItem

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen(username: String?) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {},
        bottomBar = {
            if (SunsetBottomNavItem.values().map { it.screenRoute }.contains(currentRoute)) {
                SunsetBottomNavigation(navController = navController)
            }
        }
    ) {
        NavigationGraph(navController, isLoggedIn = username != null)
    }
}