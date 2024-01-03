package com.mataku.scrobscrob.chart.ui.molecule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.ChartTrack
import com.mataku.scrobscrob.core.entity.ChartTrackArtist
import com.mataku.scrobscrob.core.entity.imageUrl
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.molecule.SunsetImage
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview

@Composable
internal fun ChartTrackCell(
  chartTrack: ChartTrack,
  rank: Int,
  modifier: Modifier = Modifier,
  onChartTrackTap: (ChartTrack) -> Unit
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .height(72.dp)
      .clickable {
        onChartTrackTap.invoke(chartTrack)
      }
      .padding(
        horizontal = 16.dp,
        vertical = 8.dp
      ),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = rank.toString(),
      style = SunsetTextStyle.caption,
      modifier = Modifier
        .width(24.dp),
      textAlign = TextAlign.Center
    )
    val imageUrl = chartTrack.imageList.imageUrl()
    val imageData = if (imageUrl == null || imageUrl.isBlank()) {
      com.mataku.scrobscrob.ui_common.R.drawable.no_image
    } else {
      imageUrl
    }

    Spacer(modifier = Modifier.width(8.dp))

    SunsetImage(
      imageData = imageData,
      contentDescription = "rank ${rank}: ${chartTrack.name} artwork",
      modifier = Modifier.size(56.dp),
      size = 1000
    )

    Column(
      modifier = Modifier
        .padding(start = 16.dp)
        .fillMaxHeight()
        .fillMaxWidth(),
      verticalArrangement = Arrangement.Center
    ) {
      Text(
        text = chartTrack.name,
        modifier = Modifier,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = SunsetTextStyle.body.copy(
          fontWeight = FontWeight.Medium
        )
      )

      Text(
        text = chartTrack.artist.name,
        maxLines = 1,
        color = Color.White,
        modifier = Modifier,
        overflow = TextOverflow.Ellipsis,
        style = SunsetTextStyle.caption
      )
    }
  }
}

@Composable
@Preview(showBackground = true)
private fun ChartTrackCellPreview() {
  SunsetThemePreview {
    Surface {
      ChartTrackCell(
        chartTrack = ChartTrack(
          name = "Drama",
          playCount = "10000000",
          listeners = "10000000",
          url = "",
          imageList = emptyList(),
          artist = ChartTrackArtist(
            name = "aespa",
            url = ""
          ),
          mbid = ""
        ),
        rank = 1,
        onChartTrackTap = {})
    }
  }
}
