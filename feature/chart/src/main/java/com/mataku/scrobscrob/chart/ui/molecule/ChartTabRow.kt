package com.mataku.scrobscrob.chart.ui.molecule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.chart.ui.ChartType
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import com.mataku.scrobscrob.ui_common.style.accentColor

@Composable
internal fun ChartTabRow(
  selectedChartIndex: Int,
  onChartTypeTap: (Int, ChartType) -> Unit,
  modifier: Modifier = Modifier
) {
  TabRow(
    selectedTabIndex = selectedChartIndex,
    containerColor = Color.Transparent,
    indicator = {},
    divider = {},
    modifier = modifier
  ) {
    repeat(2) {
      val chartType = ChartType.findByIndex(it)
      Tab(
        selected = selectedChartIndex == it,
        onClick = {
          onChartTypeTap.invoke(it, chartType)
        },
        modifier = Modifier
          .wrapContentWidth()
          .padding(
            horizontal = 12.dp,
            vertical = 4.dp
          )
      ) {
        if (it == selectedChartIndex) {
          Box(
            modifier = Modifier
              .clip(CircleShape)
              .background(color = LocalAppTheme.current.accentColor())
              .align(
                alignment = if (it == 0) {
                  Alignment.End
                } else {
                  Alignment.Start
                }
              )
              .padding(
                horizontal = 16.dp,
                vertical = 8.dp
              )
          ) {
            Text(
              text = chartType.tabName,
              color = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier
            )
          }
        } else {
          Text(
            text = ChartType.findByIndex(it).tabName,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
              .align(
                alignment = if (it == 0) {
                  Alignment.End
                } else {
                  Alignment.Start
                }
              )
              .padding(
                horizontal = 16.dp,
                vertical = 8.dp
              ),
            fontWeight = FontWeight.Medium
          )
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun ChartTabRowPreview() {
  SunsetThemePreview {
    Surface {
      ChartTabRow(
        selectedChartIndex = 0,
        onChartTypeTap = { _, _ -> }
      )
    }
  }
}
