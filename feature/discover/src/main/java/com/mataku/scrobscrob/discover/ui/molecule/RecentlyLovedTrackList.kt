package com.mataku.scrobscrob.discover.ui.molecule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.ChartTrack
import com.mataku.scrobscrob.core.entity.ChartTrackArtist
import com.mataku.scrobscrob.core.entity.LovedTrack
import com.mataku.scrobscrob.core.entity.imageUrl
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun RecentlyLovedTrackList(
  lovedTrackList: ImmutableList<LovedTrack>,
  onLovedTrackTap: (LovedTrack) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(modifier = modifier.fillMaxWidth()) {
    Text(
      text = "Your Recently Loved Tracks",
      style = SunsetTextStyle.headline,
      maxLines = 1,
      modifier = Modifier
        .padding(
          horizontal = 16.dp,
          vertical = 8.dp
        )
    )

    LazyRow(
      modifier = Modifier.fillMaxWidth(),
      contentPadding = PaddingValues(
        horizontal = 16.dp
      ),
      horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      itemsIndexed(
        lovedTrackList,
        key = { index, item -> "${index}_${item.name}" }) { index, track ->
        val cachedImageUrl = track.imageUrl
        ChartCell(
          title = track.name,
          subTitle = track.artist,
          artworkUrl = if (cachedImageUrl.isNullOrEmpty()) track.images.imageUrl() else cachedImageUrl,
          modifier = Modifier
            .clickable {
              onLovedTrackTap.invoke(track)
            }
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun ChartTrackList2Preview() {
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
