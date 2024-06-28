package com.mataku.scrobscrob.ui_common.molecule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.accentColor

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
      Text(
        text = tabName,
        style = SunsetTextStyle.label,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier,
        fontWeight = FontWeight.Bold
      )
    }
  } else {
    Text(
      text = tabName,
      style = SunsetTextStyle.label,
      color = MaterialTheme.colorScheme.onSurface,
      modifier = modifier
        .padding(
          horizontal = 16.dp,
          vertical = 8.dp
        ),
      fontWeight = FontWeight.Bold
    )
  }
}
