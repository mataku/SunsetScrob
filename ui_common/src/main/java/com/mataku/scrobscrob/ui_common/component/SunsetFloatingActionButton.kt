package com.mataku.scrobscrob.ui_common.component

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
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
