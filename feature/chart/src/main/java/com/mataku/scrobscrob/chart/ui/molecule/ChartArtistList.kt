package com.mataku.scrobscrob.chart.ui.molecule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.ChartArtist
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun ChartArtistList(
  chartArtistList: ImmutableList<ChartArtist>,
  onChartArtistTap: (ChartArtist) -> Unit
) {
  LazyColumn(
    content = {
      itemsIndexed(chartArtistList, key = { index, item -> "${index}_${item.name}" }) {  index, artist ->
        ChartArtistCell(
          chartArtist = artist,
          rank = index + 1,
          onChartArtistTap = onChartArtistTap
        )
      }
    },
  modifier = Modifier
    .fillMaxSize(),
  contentPadding = PaddingValues(
    vertical = 8.dp
  ),
  verticalArrangement = Arrangement.spacedBy(8.dp)
  )
}

@Preview(showBackground = true)
@Composable
private fun ChartArtistListPreview() {
  SunsetThemePreview {
    Surface {
      ChartArtistList(
        chartArtistList = (1..10).map {
          ChartArtist(
            name = "artist name $it",
            url = "",
            listeners = "100000$it",
            playCount = "100000$it",
            imageList = emptyList()
          )
        }.toImmutableList(),
        onChartArtistTap = {}
      )
    }
  }
}
