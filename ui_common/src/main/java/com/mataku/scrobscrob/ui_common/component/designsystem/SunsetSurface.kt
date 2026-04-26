package com.mataku.scrobscrob.ui_common.component.designsystem

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview

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

@Preview
@ShowkaseComposable(name = "SunsetSurface", group = "Design system")
@Composable
internal fun SunsetSurfacePreview() {
  SunsetThemePreview {
    SunsetSurface(shadowElevation = 2.dp) {
      SunsetText.Body(
        text = "Surface",
        modifier = Modifier.padding(16.dp),
      )
    }
  }
}
