package com.mataku.scrobscrob.ui_common.component.designsystem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
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

@Preview
@ShowkaseComposable(name = "SunsetHorizontalDivider", group = "Design system")
@Composable
internal fun SunsetHorizontalDividerPreview() {
  SunsetThemePreview {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
    ) {
      SunsetText.Body(text = "Above")
      SunsetHorizontalDivider()
      SunsetText.Body(text = "Below")
    }
  }
}
