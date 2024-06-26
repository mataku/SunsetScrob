package com.mataku.scrobscrob.ui_common.organism

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview

@Composable
fun ContentHeader(
  text: String,
  modifier: Modifier = Modifier,
  onBackPressed: (() -> Unit)? = null,
) {
  Surface(
    shadowElevation = 2.dp,
    modifier = modifier
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(64.dp)
        .background(
          MaterialTheme.colorScheme.background
        ),
      horizontalArrangement = Arrangement.Start,
      verticalAlignment = Alignment.CenterVertically
    ) {
      if (onBackPressed != null) {
        Spacer(modifier = Modifier.width(16.dp))
        Image(
          painter = rememberVectorPainter(image = Icons.AutoMirrored.Default.ArrowBack),
          contentDescription = "back",
          modifier = Modifier
            .clickable(
              indication = null,
              interactionSource = remember { MutableInteractionSource() }
            ) {
              onBackPressed.invoke()
            }
            .alpha(0.9F),
          colorFilter = ColorFilter.tint(
            color = MaterialTheme.colorScheme.onSurface
          )
        )
      }
      Text(
        text = text,
        style = SunsetTextStyle.title,
        modifier = Modifier
          .padding(horizontal = 16.dp),
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
private fun ContentHeaderPreview() {
  SunsetThemePreview {
    ContentHeader(text = "Scrobble")
  }
}

@Preview
@ShowkaseComposable(name = "ContentHeader", group = "Common", styleName = "Light")
@Composable
private fun ContentHeaderLightPreview() {
  SunsetThemePreview(theme = AppTheme.LIGHT) {
    ContentHeader(text = "Scrobble")
  }
}

@Preview
@ShowkaseComposable(name = "ContentHeader", group = "Common", styleName = "Midnight")
@Composable
private fun ContentHeaderMidnightPreview() {
  SunsetThemePreview(theme = AppTheme.MIDNIGHT) {
    ContentHeader(text = "Scrobble")
  }
}

@Preview
@ShowkaseComposable(name = "ContentHeader", group = "Common", styleName = "Last.fm Dark")
@Composable
private fun ContentHeaderLastFmDarkPreview() {
  SunsetThemePreview(theme = AppTheme.LASTFM_DARK) {
    ContentHeader(
      text = "Scrobble",
      onBackPressed = {},
      modifier = Modifier
    )
  }
}
