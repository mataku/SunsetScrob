package com.mataku.scrobscrob.discover.ui.molecule

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetText
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetImage
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview

@Composable
internal fun ChartCell(
  title: String,
  subTitle: String,
  artworkUrl: String?,
  modifier: Modifier = Modifier,
  cellSize: Dp = 140.dp
) {
  Column(
    modifier = modifier
      .width(cellSize)
  ) {
    SunsetImage(
      imageData = artworkUrl,
      contentDescription = "$title artwork",
      modifier = Modifier
        .size(cellSize)
    )
    Spacer(modifier = Modifier.height(8.dp))
    SunsetText.Body(
      text = title,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis
    )
    Spacer(modifier = Modifier.height(8.dp))
    SunsetText.Caption(
      text = subTitle,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun ChartCellPreview() {
  SunsetThemePreview {
    ChartCell(
      title = "Title",
      subTitle = "SubTitle",
      artworkUrl = ""
    )
  }
}
