package com.mataku.scrobscrob.ui_common.component.designsystem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import com.mataku.scrobscrob.ui_common.style.accentColor

@Composable
fun SunsetFloatingActionButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  shape: Shape = FloatingActionButtonDefaults.shape,
  containerColor: Color = LocalAppTheme.current.accentColor(),
  content: @Composable () -> Unit,
) {
  FloatingActionButton(
    onClick = onClick,
    modifier = modifier,
    shape = shape,
    containerColor = containerColor,
    content = content,
  )
}

@Preview
@Composable
private fun SunsetFloatingActionButtonPreview() {
  SunsetThemePreview {
    Box(modifier = Modifier.padding(16.dp)) {
      SunsetFloatingActionButton(onClick = {}) {
        SunsetIcon(
          imageVector = Icons.Default.Add,
          contentDescription = "add",
        )
      }
    }
  }
}
