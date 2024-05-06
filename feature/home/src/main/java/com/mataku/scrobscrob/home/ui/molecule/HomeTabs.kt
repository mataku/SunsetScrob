package com.mataku.scrobscrob.home.ui.molecule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.home.HomeTabType
import com.mataku.scrobscrob.ui_common.molecule.TabRowText
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import com.mataku.scrobscrob.ui_common.style.accentColor

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
    indicator = { tabPositions ->
      Box(
        modifier = Modifier
          .tabIndicatorOffset(tabPositions[selectedChartIndex])
          .height(3.dp)
          .padding(
            horizontal = 16.dp
          )
          .background(
            LocalAppTheme.current.accentColor(),
            RoundedCornerShape(100, 100, 0, 0)
          )
      )
    },
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
          selected = false,
          tabName = homeTabType.tabName,
          modifier = Modifier
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
