package com.mataku.scrobscrob.ui_common.component.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
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

@Preview
@ShowkaseComposable(name = "SunsetSwitch", group = "Design system")
@Composable
internal fun SunsetSwitchPreview() {
  SunsetThemePreview {
    Row(
      modifier = Modifier.padding(16.dp),
      horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      SunsetSwitch(checked = true, onCheckedChange = {})
      SunsetSwitch(checked = false, onCheckedChange = {})
      SunsetSwitch(checked = true, onCheckedChange = {}, enabled = false)
      SunsetSwitch(checked = false, onCheckedChange = {}, enabled = false)
    }
  }
}
