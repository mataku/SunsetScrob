package com.mataku.scrobscrob.account.ui.molecule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.UserInfo
import com.mataku.scrobscrob.core.entity.imageUrl
import com.mataku.scrobscrob.core.entity.presentation.toReadableIntValue
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.molecule.SunsetImage
import com.mataku.scrobscrob.ui_common.molecule.ValueDescription
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview

@Composable
internal fun Profile(
  userInfo: UserInfo,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(
        vertical = 12.dp
      )
    ,
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    val imageUrl = userInfo.imageList.imageUrl()
    SunsetImage(
      imageData = imageUrl,
      contentDescription = "profile image",
      modifier = Modifier
        .size(96.dp)
        .clip(CircleShape)
    )

    Column(
      verticalArrangement = Arrangement.Center,
      modifier = Modifier
        .height(96.dp)
    ) {
      Text(
        text = userInfo.name,
        style = SunsetTextStyle.title
      )

      Spacer(modifier = Modifier.height(16.dp))

      UserListeningCount(
        playCount = userInfo.playCount.toReadableIntValue(),
        albumCount = userInfo.albumCount.toReadableIntValue(),
        trackCount = userInfo.trackCount.toReadableIntValue()
      )
    }
  }
}

@Composable
private fun UserListeningCount(
  playCount: String,
  albumCount: String,
  trackCount: String,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(32.dp)
  ) {
    ValueDescription(
      value = playCount,
      label = "Scrobbles",
      modifier = Modifier
    )

    ValueDescription(
      value = albumCount,
      label = "Albums",
      modifier = modifier
    )

    ValueDescription(
      value = trackCount,
      label = "Tracks",
      modifier = modifier
    )
  }
}

@Preview
@Composable
private fun ProfilePreview() {
  SunsetThemePreview {
    Surface {
      Profile(
        userInfo = UserInfo(
          name = "mataku",
          playCount = "1000",
          albumCount = "100",
          trackCount = "100",
          imageList = listOf(),
          url = "",
          artistCount = "100"
        )
      )
    }
  }
}
