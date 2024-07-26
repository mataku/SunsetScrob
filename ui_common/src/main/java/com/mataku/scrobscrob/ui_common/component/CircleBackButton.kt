package com.mataku.scrobscrob.ui_common.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp

@Composable
fun CircleBackButton(
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(CircleShape)
      .size(
        40.dp
      )
      .background(
        color = MaterialTheme.colorScheme.background.copy(
          alpha = 0.5F
        )
      ),
    contentAlignment = Alignment.Center
  ) {
    Image(
      painter = rememberVectorPainter(image = Icons.AutoMirrored.Default.ArrowBack),
      contentDescription = "back",
      modifier = Modifier
        .alpha(0.9F),
      colorFilter = ColorFilter.tint(
        color = MaterialTheme.colorScheme.onSurface
      )
    )
  }
}
