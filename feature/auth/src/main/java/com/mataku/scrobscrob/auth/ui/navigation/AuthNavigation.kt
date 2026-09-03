package com.mataku.scrobscrob.auth.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.auth.ui.screen.LoginScreen
import com.mataku.scrobscrob.auth.ui.viewmodel.LoginViewModel
import com.mataku.scrobscrob.auth.webauth.LastFmWebAuthLauncher
import com.mataku.scrobscrob.ui_common.navigation.LoginKey
import com.mataku.scrobscrob.ui_common.navigation.PrivacyPolicyKey
import com.mataku.scrobscrob.ui_common.navigation.SunsetNavBuilder
import com.mataku.scrobscrob.ui_common.navigation.viewModelFor

fun SunsetNavBuilder.authGraph(webAuthLauncher: LastFmWebAuthLauncher) {
  destination<LoginKey> { key ->
    LoginScreen(
      viewModel = viewModelFor<LoginViewModel>(key),
      webAuthLauncher = webAuthLauncher,
      navigateToHomeFromAuth = { /* 認証完了は :app の SunsetMainScreen が isAuthenticated 切替で処理 */ },
      navigateToPrivacyPolicy = { navigate(PrivacyPolicyKey) },
      modifier = Modifier.padding(top = 24.dp),
    )
  }
}
