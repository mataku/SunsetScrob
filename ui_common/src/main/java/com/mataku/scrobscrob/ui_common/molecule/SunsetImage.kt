package com.mataku.scrobscrob.ui_common.molecule

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun SunsetImage(
  imageData: Any?,
  contentDescription: String?,
  size: Int? = null,
  modifier: Modifier
) {
  val imageRequestBuilder = ImageRequest.Builder(LocalContext.current)
    .data(imageData)
    .crossfade(300)

  if (size != null) {
    imageRequestBuilder.size(size)
  }
  AsyncImage(
    model = imageRequestBuilder
      .build(),
    contentDescription = contentDescription,
    modifier = modifier
  )
}