package com.mataku.scrobscrob.ui_common.component.designsystem

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import com.mataku.scrobscrob.ui_common.style.backgroundColor
import com.mataku.scrobscrob.ui_common.style.onSurfaceColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SunsetTabRow(
  selectedTabIndex: Int,
  modifier: Modifier = Modifier,
  containerColor: Color = Color.Transparent,
  contentColor: Color = LocalAppTheme.current.backgroundColor(),
  indicator: @Composable (selectedIndicatorModifier: Modifier) -> Unit = {},
  divider: @Composable () -> Unit = { SunsetHorizontalDivider() },
  tabs: @Composable () -> Unit,
) {
  PrimaryTabRow(
    selectedTabIndex = selectedTabIndex,
    modifier = modifier,
    containerColor = containerColor,
    contentColor = contentColor,
    indicator = {
      indicator(Modifier.tabIndicatorOffset(selectedTabIndex, matchContentSize = false))
    },
    divider = divider,
    tabs = tabs,
  )
}

@Composable
fun SunsetTab(
  selected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  content: @Composable ColumnScope.() -> Unit,
) {
  Tab(
    selected = selected,
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    content = content,
  )
}

@Preview
@Composable
private fun SunsetTabRowPreview() {
  SunsetThemePreview {
    SunsetTabRow(selectedTabIndex = 0) {
      SunsetTab(selected = true, onClick = {}) {
        SunsetText.Label(
          text = "Tab A",
          modifier = Modifier.padding(16.dp),
          color = LocalAppTheme.current.onSurfaceColor(),
        )
      }
      SunsetTab(selected = false, onClick = {}) {
        SunsetText.Label(
          text = "Tab B",
          modifier = Modifier.padding(16.dp),
          color = LocalAppTheme.current.onSurfaceColor(),
          )
      }
    }
  }
}
