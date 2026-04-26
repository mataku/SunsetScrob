package com.mataku.scrobscrob.ui_common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetText
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.accentColor
import com.mataku.scrobscrob.ui_common.style.onSurfaceColor

@Composable
fun ColumnScope.TabRowText(
  selected: Boolean,
  tabName: String,
  modifier: Modifier = Modifier,
) {
  if (selected) {
    Box(
      modifier = modifier
        .clip(CircleShape)
        .background(color = LocalAppTheme.current.accentColor())
        .padding(
          horizontal = 16.dp,
          vertical = 8.dp
        )
    ) {
      SunsetText.Label(
        text = tabName,
        color = LocalAppTheme.current.onSurfaceColor(),
        fontWeight = FontWeight.Bold,
      )
    }
  } else {
    SunsetText.Label(
      text = tabName,
      color = LocalAppTheme.current.onSurfaceColor(),
      fontWeight = FontWeight.Bold,
      modifier = modifier
        .padding(
          horizontal = 16.dp,
          vertical = 8.dp
        ),
    )
  }
}
