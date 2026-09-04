package com.mataku.scrobscrob.scrobble.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetIcon
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.onPrimaryColor

@Composable
actual fun EqualizerAnimation(modifier: Modifier) {
  SunsetIcon(
    imageVector = Icons.Filled.GraphicEq,
    contentDescription = null,
    tint = LocalAppTheme.current.onPrimaryColor(),
    modifier = modifier
  )
}
