package com.mataku.scrobscrob.ui_common.molecule

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.mataku.scrobscrob.ui_common.R
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SunsetImage(
  imageData: String?,
  contentDescription: String?,
  modifier: Modifier = Modifier,
  contentScale: ContentScale = ContentScale.Fit,
) {
  if (LocalInspectionMode.current || imageData.isNullOrEmpty()) {
    Box(
      modifier = modifier
        .border(
          width = 0.5.dp,
          color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
        )
    ) {
      Image(
        imageVector = ImageVector.vectorResource(id = R.drawable.no_image),
        contentDescription = contentDescription,
        modifier = Modifier,
        contentScale = contentScale,
        colorFilter = ColorFilter.tint(
          color = MaterialTheme.colorScheme.onSurface
        )
      )
    }
  } else {
    GlideImage(
      model = imageData,
      modifier = modifier,
      contentDescription = contentDescription,
      contentScale = contentScale,
      transition = CrossFade(animationSpec = tween(500)),
      failure = placeholder(R.drawable.no_image)
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun SunsetImagePreview() {
  SunsetThemePreview {
    Surface {
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
}
