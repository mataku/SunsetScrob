package com.mataku.scrobscrob.app.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.mataku.scrobscrob.app.ui.molecule.SunsetBottomNavItem
import com.mataku.scrobscrob.app.ui.screen.LoginScreen
import com.mataku.scrobscrob.app.ui.screen.ScrobbleScreen
import com.mataku.scrobscrob.app.ui.screen.TopAlbumsScreen
import com.mataku.scrobscrob.app.ui.screen.TopArtistsScreen
import com.mataku.scrobscrob.app.ui.viewmodel.LoginViewModel
import com.mataku.scrobscrob.app.ui.viewmodel.TopAlbumsViewModel
import com.mataku.scrobscrob.app.ui.viewmodel.TopArtistsViewModel
import com.mataku.scrobscrob.ui_common.template.WebViewScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationGraph(navController: NavHostController) {
    AnimatedNavHost(
        navController = navController,
        startDestination = SunsetBottomNavItem.SCROBBLE.screenRoute
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
            ScrobbleScreen()
        }
        composable(
            SunsetBottomNavItem.TOP_ALBUMS.screenRoute
        ) {
            val topAlbumsViewModel = hiltViewModel<TopAlbumsViewModel>()
            TopAlbumsScreen(navController, topAlbumsViewModel)
        }
        composable(SunsetBottomNavItem.TOP_ARTISTS.screenRoute) {
            val topArtistsViewModel = hiltViewModel<TopArtistsViewModel>()
            TopArtistsScreen(navController, topArtistsViewModel)
        }
        composable(
            "webview?url={url}",
            arguments = listOf(navArgument("url") {
                defaultValue = ""
            }),
            enterTransition = {
                null
            },
            popExitTransition = {
                slideOutVertically(targetOffsetY = { 1000 })
            }
        ) {
            WebViewScreen(navController = navController, url = it.arguments?.getString("url")!!)
        }
        composable("login") {
            val loginViewModel = hiltViewModel<LoginViewModel>()
            LoginScreen(navController = navController, viewModel = loginViewModel)
        }
    }
}