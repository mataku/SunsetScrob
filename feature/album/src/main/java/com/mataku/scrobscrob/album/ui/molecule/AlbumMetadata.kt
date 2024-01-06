package com.mataku.scrobscrob.album.ui.molecule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.presentation.toReadableIntValue
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.molecule.ValueDescription
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
      Text(
        text = albumName,
        style = SunsetTextStyle.body,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = artistName,
        style = SunsetTextStyle.caption,
        modifier = Modifier,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
    }

    Spacer(
      modifier = Modifier
        .width(32.dp)
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
        label = "Plays"
      )
    }
  }
}

@Composable
@Preview(showBackground = true)
private fun AlbumMetaDataPreview() {
  SunsetThemePreview {
    Surface {
      AlbumMetaData(
        albumName = "Drama",
        artistName = "aespa",
        listeners = "1000",
        playCount = "1000"
      )
    }
  }
}

@Composable
@Preview(showBackground = true)
private fun AlbumMetaDataPreloadPreview() {
  SunsetThemePreview {
    Surface {
      AlbumMetaData(
        albumName = "Drama",
        artistName = "aespa",
        listeners = null,
        playCount = null
      )
    }
  }
}
