package com.mataku.scrobscrob.ui_common.component

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SunsetSurface(
  modifier: Modifier = Modifier,
  shadowElevation: Dp = 0.dp,
  content: @Composable () -> Unit,
) {
  Surface(
    modifier = modifier,
    shadowElevation = shadowElevation,
    content = content,
  )
}
