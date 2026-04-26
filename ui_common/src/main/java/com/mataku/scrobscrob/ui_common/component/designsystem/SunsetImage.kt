package com.mataku.scrobscrob.ui_common.component.designsystem

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.mataku.scrobscrob.ui_common.R
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import com.mataku.scrobscrob.ui_common.style.onSurfaceColor

@Composable
fun SunsetImage(
  imageData: String?,
  contentDescription: String?,
  modifier: Modifier = Modifier,
  contentScale: ContentScale = ContentScale.Fit,
  skipCrossFade: Boolean = false // It may be good if applied explicitly by checking Modifier's value (e.g. sharedElement) instead of argument specification
) {
  if (LocalInspectionMode.current || imageData.isNullOrEmpty()) {
    Box(
      modifier = modifier
        .border(
          width = 0.5.dp,
          color = LocalAppTheme.current.onSurfaceColor().copy(alpha = 0.1f)
        )
    ) {
      Image(
        imageVector = ImageVector.vectorResource(id = R.drawable.no_image),
        contentDescription = contentDescription,
        modifier = Modifier,
        contentScale = contentScale,
        colorFilter = ColorFilter.tint(
          color = LocalAppTheme.current.onSurfaceColor()
        )
      )
    }
  } else {
    AsyncImage(
      model = imageData,
      modifier = modifier,
      contentDescription = contentDescription,
      contentScale = contentScale,
      error = painterResource(R.drawable.no_image),
    )
  }
}

@Preview(showBackground = true)
@ShowkaseComposable(name = "SunsetImage", group = "Design system")
@Composable
internal fun SunsetImagePreview() {
  SunsetThemePreview {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(
          16.dp
        )
    ) {
      SunsetImage(
        imageData = "https://example.com/image.jpg",
        contentDescription = "Sunset image"
      )
    }
  }
}
