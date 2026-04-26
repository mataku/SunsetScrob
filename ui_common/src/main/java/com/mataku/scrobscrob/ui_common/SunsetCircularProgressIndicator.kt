package com.mataku.scrobscrob.ui_common

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.accentColor

@Composable
fun SunsetCircularProgressIndicator(
  modifier: Modifier = Modifier,
  color: Color = LocalAppTheme.current.accentColor(),
) {
  CircularProgressIndicator(
    modifier = modifier,
    color = color,
  )
}
