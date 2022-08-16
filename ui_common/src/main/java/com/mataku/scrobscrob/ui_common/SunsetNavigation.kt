package com.mataku.scrobscrob.ui_common

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.mataku.scrobscrob.ui_common.template.WebViewScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.commonGraph() {
  composable(
    "webview?url={url}",
    arguments = listOf(navArgument("url") {
      defaultValue = ""
    })
  ) {
    WebViewScreen(url = it.arguments?.getString("url")!!, modifier = Modifier.fillMaxSize())
  }
}

fun NavController.navigateToScrobble() {
  navigate(
    SCROBBLE_DESTINATION
  ) {
    launchSingleTop = true
    popUpTo(
      graph.findStartDestination().id
    ) {
      inclusive = true
    }
  }
}

fun NavController.navigateToLogin() {
  navigate(LOGIN_DESTINATION)
}

fun NavController.navigateToPrivacyPolicy() {
  navigate(PRIVACY_POLICY_DESTINATION)
}

fun NavController.navigateToWebView(url: String) {
  navigate("webview?url=$url")
}

private const val SCROBBLE_DESTINATION = "scrobble"
const val LOGIN_DESTINATION = "login"
const val PRIVACY_POLICY_DESTINATION = "privacy_policy"
