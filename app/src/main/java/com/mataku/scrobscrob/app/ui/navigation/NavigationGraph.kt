package com.mataku.scrobscrob.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mataku.scrobscrob.app.ui.molecule.SunsetBottomNavItem
import com.mataku.scrobscrob.app.ui.screen.ScrobbleScreen
import com.mataku.scrobscrob.app.ui.screen.TopAlbumsScreen
import com.mataku.scrobscrob.app.ui.screen.TopArtistsScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = SunsetBottomNavItem.SCROBBLE.screenRoute
    ) {
        composable(SunsetBottomNavItem.SCROBBLE.screenRoute) {
            ScrobbleScreen()
        }
        composable(SunsetBottomNavItem.TOP_ALBUMS.screenRoute) {
            TopAlbumsScreen()
        }
        composable(SunsetBottomNavItem.TOP_ARTISTS.screenRoute) {
            TopArtistsScreen()
        }
    }
}