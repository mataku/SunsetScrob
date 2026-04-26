package com.mataku.scrobscrob.ui_common.component

import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.onSecondaryColor

@Composable
fun SunsetHorizontalDivider(
  modifier: Modifier = Modifier,
  thickness: Dp = DividerDefaults.Thickness,
  color: Color = LocalAppTheme.current.onSecondaryColor().copy(alpha = 0.4F),
) {
  HorizontalDivider(
    modifier = modifier,
    thickness = thickness,
    color = color,
  )
}
