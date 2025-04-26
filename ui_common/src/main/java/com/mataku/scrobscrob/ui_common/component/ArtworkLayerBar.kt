package com.mataku.scrobscrob.ui_common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme

/*
   A Layer to make the status bar easier to see.
   It may be difficult to see if artwork background and status bar text color are similar
 */
@Composable
fun ColumnScope.ArtworkLayerBar(
  modifier: Modifier = Modifier
) {
  val theme = LocalAppTheme.current
  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(48.dp)
      .background(
        Brush.verticalGradient(
          colors = listOf(
            MaterialTheme.colorScheme.surface.copy(alpha = 0.9F),
            if (theme.isLight) {
              Color(0xFFF5F5F5).copy(alpha = 0.2F)
            } else {
              MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05F)
            }
          )
        )
      ),
    contentAlignment = Alignment.Center,
  ) {}
}
