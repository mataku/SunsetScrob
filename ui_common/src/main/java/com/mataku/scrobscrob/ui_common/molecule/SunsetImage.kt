package com.mataku.scrobscrob.ui_common.molecule

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.size.Size

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

  val imageRequestBuilder = ImageRequest.Builder(LocalContext.current)
    .data(imageData)
    .size(Size.ORIGINAL)

  SubcomposeAsyncImage(
    model = imageRequestBuilder.build(),
    contentDescription = contentDescription,
    modifier = modifier,
    loading = {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            MaterialTheme.colorScheme.background
          )
      )
    }
  )
}
