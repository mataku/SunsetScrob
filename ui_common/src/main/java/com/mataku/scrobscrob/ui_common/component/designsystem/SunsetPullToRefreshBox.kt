package com.mataku.scrobscrob.ui_common.component.designsystem

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SunsetPullToRefreshBox(
  isRefreshing: Boolean,
  onRefresh: () -> Unit,
  modifier: Modifier = Modifier,
  content: @Composable BoxScope.() -> Unit,
) {
  PullToRefreshBox(
    isRefreshing = isRefreshing,
    onRefresh = onRefresh,
    modifier = modifier,
    content = content,
  )
}

@Preview
@ShowkaseComposable(name = "SunsetPullToRefreshBox", group = "Design system")
@Composable
internal fun SunsetPullToRefreshBoxPreview() {
  SunsetThemePreview {
    SunsetPullToRefreshBox(
      isRefreshing = true,
      onRefresh = {},
      modifier = Modifier
        .fillMaxWidth()
        .height(300.dp),
    ) {
      SunsetText.Body(
        text = "Pull to refresh",
        modifier = Modifier.padding(16.dp),
      )
    }
  }
}
