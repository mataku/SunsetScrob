package com.mataku.scrobscrob.ui_common.component.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview

@Composable
fun SunsetIcon(
  imageVector: ImageVector,
  contentDescription: String?,
  modifier: Modifier = Modifier,
  tint: Color = LocalContentColor.current,
) {
  Icon(
    imageVector = imageVector,
    contentDescription = contentDescription,
    modifier = modifier,
    tint = tint,
  )
}

@Composable
fun SunsetIconButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  content: @Composable () -> Unit,
) {
  IconButton(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    content = content,
  )
}

@Composable
fun SunsetIconToggleButton(
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  content: @Composable () -> Unit,
) {
  IconToggleButton(
    checked = checked,
    onCheckedChange = onCheckedChange,
    modifier = modifier,
    enabled = enabled,
    content = content,
  )
}

@Preview
@Composable
private fun SunsetIconPreview() {
  SunsetThemePreview {
    Row(
      modifier = Modifier.padding(16.dp),
      horizontalArrangement = Arrangement.spacedBy(16.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      SunsetIcon(
        imageVector = Icons.Default.Star,
        contentDescription = null,
      )
      SunsetIconButton(onClick = {}) {
        SunsetIcon(
          imageVector = Icons.Default.Favorite,
          contentDescription = null,
        )
      }
      SunsetIconToggleButton(checked = true, onCheckedChange = {}) {
        SunsetIcon(
          imageVector = Icons.Default.Check,
          contentDescription = null,
        )
      }
    }
  }
}
