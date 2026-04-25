package com.mataku.scrobscrob.ui_common

import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Composable
fun SunsetHorizontalDivider(
  modifier: Modifier = Modifier,
  thickness: Dp = DividerDefaults.Thickness,
  color: Color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.4F),
) {
  HorizontalDivider(
    modifier = modifier,
    thickness = thickness,
    color = color,
  )
}
