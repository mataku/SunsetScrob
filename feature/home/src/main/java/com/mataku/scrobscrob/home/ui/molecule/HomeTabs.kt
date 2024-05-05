package com.mataku.scrobscrob.home.ui.molecule

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.home.HomeTabType
import com.mataku.scrobscrob.ui_common.molecule.TabRowText
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview

@Composable
internal fun HomeTabs(
  selectedChartIndex: Int,
  onTabTap: (HomeTabType) -> Unit,
  modifier: Modifier = Modifier
) {
  TabRow(
    selectedTabIndex = selectedChartIndex,
    containerColor = Color.Transparent,
    contentColor = MaterialTheme.colorScheme.background,
    indicator = {},
    divider = {
      HorizontalDivider()
    },
    modifier = modifier
  ) {
    repeat(3) {
      val homeTabType = HomeTabType.findByIndex(it)
      Tab(
        selected = selectedChartIndex == it,
        onClick = {
          onTabTap.invoke(homeTabType)
        },
        modifier = Modifier
          .wrapContentWidth()
          .padding(
            horizontal = 12.dp,
            vertical = 4.dp
          )
      ) {
        TabRowText(
          selected = it == selectedChartIndex,
          tabName = homeTabType.tabName,
          modifier = Modifier
            .align(
              alignment = when (it) {
                0 -> Alignment.End
                1 -> Alignment.CenterHorizontally
                else -> Alignment.Start
              }
            )
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun HomeTabsPreview() {
  SunsetThemePreview {
    Surface {
      HomeTabs(
        selectedChartIndex = 0,
        onTabTap = {}
      )
    }
  }
}
