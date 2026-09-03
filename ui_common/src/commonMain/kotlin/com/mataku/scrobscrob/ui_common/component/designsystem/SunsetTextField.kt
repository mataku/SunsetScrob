package com.mataku.scrobscrob.ui_common.component.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import com.mataku.scrobscrob.ui_common.style.accentColor

@Composable
fun SunsetTextField(
  value: String,
  onValueChange: (String) -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  singleLine: Boolean = false,
  label: @Composable (() -> Unit)? = null,
  trailingIcon: @Composable (() -> Unit)? = null,
  visualTransformation: VisualTransformation = VisualTransformation.None,
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  keyboardActions: KeyboardActions = KeyboardActions.Default,
  colors: TextFieldColors = SunsetTextFieldDefaults.colors(),
) {
  OutlinedTextField(
    value = value,
    onValueChange = onValueChange,
    modifier = modifier,
    enabled = enabled,
    singleLine = singleLine,
    label = label,
    trailingIcon = trailingIcon,
    visualTransformation = visualTransformation,
    keyboardOptions = keyboardOptions,
    keyboardActions = keyboardActions,
    colors = colors,
  )
}

object SunsetTextFieldDefaults {
  @Composable
  fun colors(): TextFieldColors {
    val accent = LocalAppTheme.current.accentColor()
    return OutlinedTextFieldDefaults.colors(
      focusedBorderColor = accent,
    )
  }
}

@Preview
@Composable
internal fun SunsetTextFieldPreview() {
  SunsetThemePreview {
    Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      SunsetTextField(
        value = "",
        onValueChange = {},
        label = { SunsetText.Label(text = "Empty") },
      )
      SunsetTextField(
        value = "Hello",
        onValueChange = {},
        label = { SunsetText.Label(text = "Filled") },
      )
      SunsetTextField(
        value = "Disabled",
        onValueChange = {},
        enabled = false,
        label = { SunsetText.Label(text = "Disabled") },
      )
    }
  }
}
