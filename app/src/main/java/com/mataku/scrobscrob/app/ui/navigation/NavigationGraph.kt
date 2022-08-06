package com.mataku.scrobscrob.app.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.mataku.scrobscrob.account.ui.ThemeSelectorScreen
import com.mataku.scrobscrob.account.ui.screen.AccountScreen
import com.mataku.scrobscrob.account.ui.screen.LicenseScreen
import com.mataku.scrobscrob.account.ui.screen.PrivacyPolicyScreen
import com.mataku.scrobscrob.account.ui.state.rememberAccountState
import com.mataku.scrobscrob.account.ui.state.rememberThemeSelectorState
import com.mataku.scrobscrob.album.ui.screen.TopAlbumsScreen
import com.mataku.scrobscrob.album.ui.state.rememberTopAlbumsScreenState
import com.mataku.scrobscrob.artist.ui.screen.TopArtistsScreen
import com.mataku.scrobscrob.artist.ui.state.rememberTopArtistsScreenState
import com.mataku.scrobscrob.auth.ui.screen.LoginScreen
import com.mataku.scrobscrob.auth.ui.state.rememberLoginScreenState
import com.mataku.scrobscrob.scrobble.ui.screen.ScrobbleScreen
import com.mataku.scrobscrob.scrobble.ui.screen.TrackScreen
import com.mataku.scrobscrob.scrobble.ui.state.rememberScrobbleScreenState
import com.mataku.scrobscrob.scrobble.ui.state.rememberTrackScreenState
import com.mataku.scrobscrob.ui_common.SunsetBottomNavItem
import com.mataku.scrobscrob.ui_common.style.ANIMATION_DURATION_MILLIS
import com.mataku.scrobscrob.ui_common.template.WebViewScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationGraph(
  navController: NavHostController,
  isLoggedIn: Boolean
) {
  AnimatedNavHost(
    navController = navController,
    startDestination = if (isLoggedIn) SunsetBottomNavItem.SCROBBLE.screenRoute else "login"
  ) {
    composable(
      SunsetBottomNavItem.SCROBBLE.screenRoute,
      enterTransition = {
        fadeIn(tween(ANIMATION_DURATION_MILLIS))
      },
      exitTransition = {
        fadeOut(tween(ANIMATION_DURATION_MILLIS))
      }
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
      "track_detail?trackName={trackName}&artistName={artistName}&imageUrl={imageUrl}&upperLeftCoordinatorX={x}&upperLeftCoordinatorY={y}",
      arguments = listOf(
        navArgument("trackName") {
          type = NavType.StringType
        },
        navArgument("artistName") {
          type = NavType.StringType
        },
        navArgument("imageUrl") {
          type = NavType.StringType
        },
        navArgument("x") {
          type = NavType.IntType
        },
        navArgument("y") {
          type = NavType.IntType
        }
      ),
      content = {
        val arguments = it.arguments ?: return@composable
        val x = arguments.getInt("x", 0)
        val y = arguments.getInt("y", 0)

        val trackName = arguments.getString("trackName", "")
        val artistName = arguments.getString("artistName", "")

        TrackScreen(
          trackName = trackName,
          artistName = artistName,
          artworkUrl = arguments.getString("imageUrl", ""),
          topLeftCoordinate = Pair(x, y),
          screenState = rememberTrackScreenState(
            navController = navController,
            trackName = trackName,
            artistName = artistName
          )
        )
      }
    )

    composable(
      "webview?url={url}",
      arguments = listOf(navArgument("url") {
        defaultValue = ""
      })
    ) {
      WebViewScreen(url = it.arguments?.getString("url")!!, modifier = Modifier.fillMaxSize())
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
    composable("privacy_policy") {
      PrivacyPolicyScreen()
    }
  }
}
