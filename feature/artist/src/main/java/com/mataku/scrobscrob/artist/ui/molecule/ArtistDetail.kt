package com.mataku.scrobscrob.artist.ui.molecule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.presentation.toReadableIntValue
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.molecule.ValueDescription

@Composable
internal fun ArtistDetail(
  artistName: String,
  listeners: String?,
  playCount: String?,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(32.dp)
  ) {
    Text(
      text = artistName,
      style = SunsetTextStyle.body,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      modifier = Modifier
        .weight(1F)
    )

    Spacer(
      modifier = Modifier
        .width(16.dp)
    )
    ValueDescription(
      value = listeners?.toReadableIntValue() ?: "",
      label = "Listeners"
    )


    ValueDescription(
      value = playCount?.toReadableIntValue() ?: "",
      label = "Plays"
    )
  }
}
