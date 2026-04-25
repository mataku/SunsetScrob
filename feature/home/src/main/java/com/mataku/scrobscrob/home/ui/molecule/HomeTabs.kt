package com.mataku.scrobscrob.home.ui.molecule

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.home.HomeTabType
import com.mataku.scrobscrob.ui_common.SunsetTab
import com.mataku.scrobscrob.ui_common.SunsetTabRow
import com.mataku.scrobscrob.ui_common.SunsetText
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import com.mataku.scrobscrob.ui_common.style.accentColor

@Composable
internal fun HomeTabs(
  selectedChartIndex: Int,
  onTabTap: (HomeTabType) -> Unit,
  modifier: Modifier = Modifier
) {
  val accentColor = LocalAppTheme.current.accentColor()

  SunsetTabRow(
    selectedTabIndex = selectedChartIndex,
    indicator = { selectedIndicatorModifier ->
      Box(
        modifier = selectedIndicatorModifier
          .height(3.dp)
          .padding(
            horizontal = 16.dp
          )
          .drawBehind {
            drawRoundRect(
              color = accentColor,
              cornerRadius = CornerRadius(100f, 100f)
            )
          }
      )
    },
    modifier = modifier
  ) {
    repeat(3) {
      val homeTabType = HomeTabType.findByIndex(it)
      SunsetTab(
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
        TabText(
          selected = it == selectedChartIndex,
          tabName = homeTabType.tabName,
          modifier = Modifier
        )
      }
    }
  }
}

@Composable
private fun TabText(
  selected: Boolean,
  tabName: String,
  modifier: Modifier = Modifier,
) {
  SunsetText.Label(
    text = tabName,
    color = MaterialTheme.colorScheme.onSurface.copy(
      alpha = if (selected) 1.0F else 0.6F
    ),
    fontWeight = FontWeight.Bold,
    modifier = modifier
      .padding(
        horizontal = 16.dp,
        vertical = 8.dp
      ),
  )
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
