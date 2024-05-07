package com.mataku.scrobscrob.discover.ui.molecule

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
import com.mataku.scrobscrob.core.entity.ChartTrack
import com.mataku.scrobscrob.core.entity.ChartTrackArtist
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun ChartTrackList(
  chartTrackList: ImmutableList<ChartTrack>,
  onChartTrackTap: (ChartTrack) -> Unit
) {
  LazyColumn(
    content = {
      itemsIndexed(
        chartTrackList,
        key = { index, item -> "${index}_${item.name}" }) { index, track ->
        ChartTrackCell(
          chartTrack = track,
          rank = index + 1,
          onChartTrackTap = onChartTrackTap
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
private fun ChartTrackListPreview() {
  SunsetThemePreview {
    Surface {
      ChartTrackList(
        chartTrackList = (1..10).map {
          ChartTrack(
            name = "track name ${it}",
            url = "",
            listeners = "100000${it}",
            imageList = emptyList(),
            playCount = "100000${it}",
            artist = ChartTrackArtist(
              name = "artist name $it",
              url = ""
            ),
            mbid = ""
          )
        }.toImmutableList(),
        onChartTrackTap = {}
      )
    }
  }
}
