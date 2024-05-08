package com.mataku.scrobscrob.discover.ui.molecule

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.molecule.SunsetImage
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview

@Composable
internal fun ChartCell(
  title: String,
  subTitle: String,
  artworkUrl: String?,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .width(140.dp)
  ) {
    SunsetImage(
      imageData = artworkUrl,
      contentDescription = "$title artwork",
      modifier = Modifier
        .size(140.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
      text = title,
      style = SunsetTextStyle.body,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
      text = subTitle,
      style = SunsetTextStyle.caption,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun ChartCellPreview() {
  SunsetThemePreview {
    Surface {
      ChartCell(
        title = "Title",
        subTitle = "SubTitle",
        artworkUrl = ""
      )
    }
  }
}
