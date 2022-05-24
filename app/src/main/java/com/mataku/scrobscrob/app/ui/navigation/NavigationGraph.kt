package com.mataku.scrobscrob.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.mataku.scrobscrob.album.ui.screen.TopAlbumsScreen
import com.mataku.scrobscrob.album.ui.state.rememberTopAlbumsScreenState
import com.mataku.scrobscrob.artist.ui.screen.TopArtistsScreen
import com.mataku.scrobscrob.artist.ui.state.rememberTopArtistsScreenState
import com.mataku.scrobscrob.auth.ui.screen.LoginScreen
import com.mataku.scrobscrob.auth.ui.screen.LogoutConfirmationDialog
import com.mataku.scrobscrob.auth.ui.state.rememberLoginScreenState
import com.mataku.scrobscrob.auth.ui.state.rememberLogoutConfirmationDialogState
import com.mataku.scrobscrob.scrobble.ui.screen.ScrobbleScreen
import com.mataku.scrobscrob.scrobble.ui.state.rememberScrobbleScreenState
import com.mataku.scrobscrob.ui_common.SunsetBottomNavItem
import com.mataku.scrobscrob.ui_common.template.WebViewScreen

// @OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationGraph(navController: NavHostController, isLoggedIn: Boolean) {
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) SunsetBottomNavItem.SCROBBLE.screenRoute else "login"
//        enterTransition = {
//            slideInHorizontally(initialOffsetX = { 1000 })
//        },
//        exitTransition = {
//            val currentRoute = initialState.destination.route
//            if (currentRoute?.startsWith("webview?url") == true) {
//                slideOutVertically(targetOffsetY = { 0 })
//            } else {
//                slideOutHorizontally(targetOffsetX = { 1000 })
//            }
//        }
    ) {
        composable(
            SunsetBottomNavItem.SCROBBLE.screenRoute
        ) {
            ScrobbleScreen(
                state = rememberScrobbleScreenState(navController = navController)
            )
        }
        composable(
            SunsetBottomNavItem.TOP_ALBUMS.screenRoute
        ) {
            TopAlbumsScreen(
                state = rememberTopAlbumsScreenState(navController = navController)
            )
        }
        composable(SunsetBottomNavItem.TOP_ARTISTS.screenRoute) {
            TopArtistsScreen(
                state = rememberTopArtistsScreenState(navController = navController)
            )
        }
        composable(
            "webview?url={url}",
            arguments = listOf(navArgument("url") {
                defaultValue = ""
            })
        ) {
            WebViewScreen(navController = navController, url = it.arguments?.getString("url")!!)
        }
        composable("login") {
            LoginScreen(
                stateHolder = rememberLoginScreenState(navController = navController)
            )
        }
        dialog("logout") {
            LogoutConfirmationDialog(
                stateHolder = rememberLogoutConfirmationDialogState(navController = navController)
            )
        }
    }
}