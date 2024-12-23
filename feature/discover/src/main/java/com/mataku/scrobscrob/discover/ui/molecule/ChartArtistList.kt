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
import com.mataku.scrobscrob.core.entity.ChartArtist
import com.mataku.scrobscrob.core.entity.imageUrl
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun ChartArtistList(
  chartArtistList: ImmutableList<ChartArtist>,
  onChartArtistTap: (ChartArtist) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(modifier = modifier.fillMaxWidth()) {
    Text(
      text = "Hot Artists",
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
        chartArtistList,
        key = { index, item -> "${index}_${item.name}" }) { _, artist ->
        ChartCell(
          title = artist.name,
          subTitle = "",
          artworkUrl = artist.imageUrl ?: artist.imageList.imageUrl(),
          modifier = Modifier
            .clickable {
              onChartArtistTap.invoke(artist)
            }
        )
      }
    }
  }
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
