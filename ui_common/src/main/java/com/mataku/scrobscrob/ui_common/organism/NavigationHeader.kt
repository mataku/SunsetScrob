package com.mataku.scrobscrob.ui_common.organism

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview

@Composable
fun NavigationHeader(
  text: String,
  onBackPressed: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(64.dp)
        .background(
          MaterialTheme.colorScheme.background
        ),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Image(
        painter = rememberVectorPainter(image = Icons.AutoMirrored.Default.ArrowBack),
        contentDescription = "back",
        modifier = Modifier
          .padding(
            horizontal = 16.dp
          )
          .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
          ) {
            onBackPressed.invoke()
          },
        colorFilter = ColorFilter.tint(
          color = MaterialTheme.colorScheme.onSurface
        ),
        alignment = Alignment.Center
      )
      Text(
        text = text,
        style = SunsetTextStyle.title,
        modifier = Modifier,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
    }
  }
}

@Preview
@ShowkaseComposable(
  name = "NavigationHeader",
  group = "Common",
  defaultStyle = true,
  styleName = "Dark"
)
@Composable
private fun NavigationHeaderPreview() {
  SunsetThemePreview {
    NavigationHeader(text = "Scrobble", onBackPressed = {})
  }
}

@Preview
@ShowkaseComposable(name = "NavigationHeader", group = "Common", styleName = "Light")
@Composable
private fun NavigationHeaderLightPreview() {
  SunsetThemePreview(theme = AppTheme.LIGHT) {
    NavigationHeader(text = "Scrobble", onBackPressed = {})
  }
}

@Preview
@ShowkaseComposable(name = "NavigationHeader", group = "Common", styleName = "Midnight")
@Composable
private fun NavigationHeaderMidnightPreview() {
  SunsetThemePreview(theme = AppTheme.MIDNIGHT) {
    NavigationHeader(text = "Scrobble", onBackPressed = {})
  }
}

@Preview
@ShowkaseComposable(name = "NavigationHeader", group = "Common", styleName = "Last.fm Dark")
@Composable
private fun NavigationHeaderLastFmDarkPreview() {
  SunsetThemePreview(theme = AppTheme.LASTFM_DARK) {
    NavigationHeader(text = "Scrobble", onBackPressed = {})
  }
}
