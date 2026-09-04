package com.mataku.scrobscrob.discover.ui.navigation

import androidx.compose.ui.Modifier
import com.mataku.scrobscrob.chart.ui.navigation.DiscoverKey
import com.mataku.scrobscrob.discover.ui.screen.DiscoverScreen
import com.mataku.scrobscrob.discover.ui.viewmodel.DiscoverViewModel
import com.mataku.scrobscrob.ui_common.navigation.SunsetNavBuilder
import com.mataku.scrobscrob.ui_common.navigation.WebViewKey
import com.mataku.scrobscrob.ui_common.navigation.viewModelFor

fun SunsetNavBuilder.discoverGraph() {
  destination<DiscoverKey> {
    DiscoverScreen(
      viewModel = viewModelFor<DiscoverViewModel>(DiscoverKey),
      navigateToWebView = { url -> navigate(WebViewKey(url)) },
      modifier = Modifier,
    )
  }
}
