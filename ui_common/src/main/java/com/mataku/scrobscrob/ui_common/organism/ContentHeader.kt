package com.mataku.scrobscrob.ui_common.organism

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview

@Composable
fun ContentHeader(
  text: String
) {
  Surface(
    shadowElevation = 2.dp
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(64.dp)
        .background(
          MaterialTheme.colorScheme.background
        ),
      contentAlignment = Alignment.CenterStart
    ) {
      Text(
        text = text,
        style = SunsetTextStyle.title,
        modifier = Modifier
          .padding(horizontal = 16.dp)
      )
    }
  }
}

@Preview
@ShowkaseComposable(
  name = "ContentHeader",
  group = "Common",
  defaultStyle = true,
  styleName = "Dark"
)
@Composable
fun ContentHeaderPreview() {
  SunsetThemePreview {
    ContentHeader(text = "Scrobble")
  }
}

@Preview
@ShowkaseComposable(name = "ContentHeader", group = "Common", styleName = "Light")
@Composable
fun ContentHeaderLightPreview() {
  SunsetThemePreview(theme = AppTheme.LIGHT) {
    ContentHeader(text = "Scrobble")
  }
}

@Preview
@ShowkaseComposable(name = "ContentHeader", group = "Common", styleName = "Midnight")
@Composable
fun ContentHeaderMidnightPreview() {
  SunsetThemePreview(theme = AppTheme.MIDNIGHT) {
    ContentHeader(text = "Scrobble")
  }
}

@Preview
@ShowkaseComposable(name = "ContentHeader", group = "Common", styleName = "Last.fm Dark")
@Composable
fun ContentHeaderLastFmDarkPreview() {
  SunsetThemePreview(theme = AppTheme.LASTFM_DARK) {
    ContentHeader(text = "Scrobble")
  }
}
