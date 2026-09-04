package com.mataku.scrobscrob.ui_common.component.designsystem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
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

@Preview
@Composable
private fun SunsetCircularProgressIndicatorPreview() {
  SunsetThemePreview {
    Box(modifier = Modifier.padding(16.dp)) {
      SunsetCircularProgressIndicator()
    }
  }
}
