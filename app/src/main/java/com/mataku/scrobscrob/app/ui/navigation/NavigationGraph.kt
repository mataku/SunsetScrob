package com.mataku.scrobscrob.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mataku.scrobscrob.account.ui.ThemeSelectorScreen
import com.mataku.scrobscrob.account.ui.screen.AccountScreen
import com.mataku.scrobscrob.account.ui.screen.LicenseScreen
import com.mataku.scrobscrob.account.ui.state.rememberAccountState
import com.mataku.scrobscrob.account.ui.state.rememberThemeSelectorState
import com.mataku.scrobscrob.album.ui.screen.TopAlbumsScreen
import com.mataku.scrobscrob.album.ui.state.rememberTopAlbumsScreenState
import com.mataku.scrobscrob.artist.ui.screen.TopArtistsScreen
import com.mataku.scrobscrob.artist.ui.state.rememberTopArtistsScreenState
import com.mataku.scrobscrob.auth.ui.screen.LoginScreen
import com.mataku.scrobscrob.auth.ui.state.rememberLoginScreenState
import com.mataku.scrobscrob.scrobble.ui.screen.ScrobbleScreen
import com.mataku.scrobscrob.scrobble.ui.state.rememberScrobbleScreenState
import com.mataku.scrobscrob.ui_common.SunsetBottomNavItem
import com.mataku.scrobscrob.ui_common.template.WebViewScreen

@Composable
fun NavigationGraph(navController: NavHostController, isLoggedIn: Boolean) {
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) SunsetBottomNavItem.SCROBBLE.screenRoute else "login"
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
        composable(SunsetBottomNavItem.ACCOUNT.screenRoute) {
            AccountScreen(state = rememberAccountState(navController = navController))
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
        composable("theme") {
            ThemeSelectorScreen(
                state = rememberThemeSelectorState(
                    navController = navController
                )
            )
        }
        composable("license") {
            LicenseScreen()
        }
    }
}