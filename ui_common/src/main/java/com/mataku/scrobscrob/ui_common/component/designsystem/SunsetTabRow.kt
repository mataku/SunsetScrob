package com.mataku.scrobscrob.ui_common.component.designsystem

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.backgroundColor

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
