package com.mataku.scrobscrob.chart.ui.molecule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.ChartArtist
import com.mataku.scrobscrob.core.entity.imageUrl
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.molecule.SunsetImage
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview

@Composable
fun ChartArtistCell(
  chartArtist: ChartArtist,
  rank: Int,
  modifier: Modifier = Modifier,
  onChartArtistTap: (ChartArtist) -> Unit
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .height(72.dp)
      .clickable {
        onChartArtistTap.invoke(chartArtist)
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
      modifier = Modifier.width(24.dp)
    )
    val imageUrl = chartArtist.imageList.imageUrl()
    val imageData = if (imageUrl == null || imageUrl.isBlank()) {
      com.mataku.scrobscrob.ui_common.R.drawable.no_image
    } else {
      imageUrl
    }

    Spacer(modifier = Modifier.width(16.dp))

    SunsetImage(
      imageData = imageData,
      contentDescription = "rank ${rank}: ${chartArtist.name} artwork",
      modifier = Modifier.size(56.dp),
      size = 1000
    )


    Spacer(modifier = Modifier.width(16.dp))

    Text(
      text = chartArtist.name,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      style = SunsetTextStyle.body.copy(
        fontWeight = FontWeight.Bold
      )
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun ChartArtistPreview() {
  SunsetThemePreview {
    Surface {
      ChartArtistCell(
        chartArtist = ChartArtist(
          name = "aespa",
          listeners = "100000000",
          url = "",
          imageList = emptyList(),
          playCount = "100000000"
        ),
        rank = 1,
        onChartArtistTap = {}
      )
    }
  }
}
