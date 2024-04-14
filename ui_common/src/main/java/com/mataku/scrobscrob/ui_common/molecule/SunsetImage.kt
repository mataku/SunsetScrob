package com.mataku.scrobscrob.ui_common.molecule

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SunsetImage(
  modifier: Modifier = Modifier,
  imageData: Any?,
  contentDescription: String?,
  contentScale: ContentScale = ContentScale.Fit,
) {
  if (LocalInspectionMode.current) {
    Image(
      painter = painterResource(id = com.mataku.scrobscrob.ui_common.R.drawable.no_image),
      contentDescription = contentDescription,
      modifier = modifier,
      contentScale = contentScale
    )
    return
  }
  GlideImage(
    model = imageData,
    modifier = modifier,
    contentDescription = contentDescription,
    contentScale = contentScale,
    transition = CrossFade(animationSpec = tween(300))
  )
}
