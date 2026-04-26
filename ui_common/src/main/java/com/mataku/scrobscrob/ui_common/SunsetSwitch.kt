package com.mataku.scrobscrob.ui_common

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.accentColor
import com.mataku.scrobscrob.ui_common.style.onPrimaryColor

@Composable
fun SunsetSwitch(
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  colors: SwitchColors = SunsetSwitchDefaults.colors(),
) {
  Switch(
    checked = checked,
    onCheckedChange = onCheckedChange,
    modifier = modifier,
    enabled = enabled,
    colors = colors,
  )
}

object SunsetSwitchDefaults {
  @Composable
  fun colors(): SwitchColors {
    val accent = LocalAppTheme.current.accentColor()
    return SwitchDefaults.colors(
      checkedThumbColor = LocalAppTheme.current.onPrimaryColor(),
      checkedTrackColor = accent,
    )
  }
}
