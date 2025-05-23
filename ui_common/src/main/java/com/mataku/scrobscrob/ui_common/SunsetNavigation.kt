package com.mataku.scrobscrob.ui_common

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mataku.scrobscrob.ui_common.template.WebViewScreen

fun NavGraphBuilder.commonGraph(navController: NavController) {
  composable(
    "webview?url={url}",
    arguments = listOf(
      navArgument("url") {
        defaultValue = ""
      }
    ),
    content = {
      WebViewScreen(
        url = it.arguments?.getString("url")!!,
        modifier = Modifier
      )
    }
  )
}

fun NavController.navigateToLogin() {
  navigate(LOGIN_DESTINATION) {
    popUpTo(0)
  }
}

fun NavController.navigateToPrivacyPolicy() {
  navigate(PRIVACY_POLICY_DESTINATION)
}

fun NavController.navigateToWebView(url: String) {
  navigate("webview?url=$url")
}

const val LOGIN_DESTINATION = "login"
const val PRIVACY_POLICY_DESTINATION = "privacy_policy"
