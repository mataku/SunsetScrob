package com.mataku.scrobscrob.ui_common.molecule

import android.annotation.SuppressLint
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.vectorResource
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.mataku.scrobscrob.ui_common.R

@SuppressLint("ComposeModifierReused")
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SunsetImage(
  imageData: String?,
  contentDescription: String?,
  modifier: Modifier = Modifier,
  contentScale: ContentScale = ContentScale.Fit,
) {
  if (LocalInspectionMode.current || imageData == null) {
    Image(
      imageVector = ImageVector.vectorResource(id = R.drawable.no_image),
      contentDescription = contentDescription,
      modifier = modifier,
      contentScale = contentScale,
      colorFilter = ColorFilter.tint(
        color = MaterialTheme.colorScheme.onSurface
      )
    )
    return
  }
  GlideImage(
    model = imageData,
    modifier = modifier,
    contentDescription = contentDescription,
    contentScale = contentScale,
    transition = CrossFade(animationSpec = tween(300)),
    failure = placeholder(R.drawable.no_image)
  )
}
