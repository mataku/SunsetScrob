package com.mataku.scrobscrob.album.ui.molecule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.presentation.toReadableIntValue
import com.mataku.scrobscrob.ui_common.component.SunsetText
import com.mataku.scrobscrob.ui_common.component.ValueDescription
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview

@Composable
internal fun AlbumMetaData(
  albumName: String,
  artistName: String,
  listeners: String?,
  playCount: String?,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(32.dp)
  ) {
    Column(
      modifier = Modifier
        .weight(1F)
    ) {
      SunsetText.Body(
        text = albumName,
        fontWeight = FontWeight.Bold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(4.dp))

      SunsetText.Caption(
        text = artistName,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
    }

    Spacer(
      modifier = Modifier
        .width(16.dp)
    )

    listeners?.let {
      ValueDescription(
        value = it.toReadableIntValue(),
        label = "Listeners"
      )
    }
    playCount?.let {
      ValueDescription(
        value = it.toReadableIntValue(),
        label = "Scrobbles"
      )
    }
  }
}

@Composable
@Preview(showBackground = true)
private fun AlbumMetaDataPreview() {
  SunsetThemePreview {
    AlbumMetaData(
      albumName = "Drama",
      artistName = "aespa",
      listeners = "1000",
      playCount = "1000"
    )
  }
}

@Composable
@Preview(showBackground = true)
private fun AlbumMetaDataPreloadPreview() {
  SunsetThemePreview {
    AlbumMetaData(
      albumName = "Drama",
      artistName = "aespa",
      listeners = null,
      playCount = null
    )
  }
}
