package com.mataku.scrobscrob.ui_common.component.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview

@Composable
fun SunsetButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
  content: @Composable RowScope.() -> Unit,
) {
  Button(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    contentPadding = contentPadding,
    content = content,
  )
}

object SunsetTextButton {
  @Composable
  operator fun invoke(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
    content: @Composable RowScope.() -> Unit,
  ) {
    TextButton(
      onClick = onClick,
      modifier = modifier,
      enabled = enabled,
      contentPadding = contentPadding,
      content = content,
    )
  }

  @Composable
  fun Label(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: Color = Color.Unspecified,
  ) {
    invoke(
      onClick = onClick,
      modifier = modifier,
      enabled = enabled,
    ) {
      SunsetText.ButtonLabel(
        text = text,
        color = color,
      )
    }
  }
}

@Preview
@Composable
internal fun SunsetButtonPreview() {
  SunsetThemePreview {
    Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      SunsetButton(onClick = {}) {
        SunsetText.ButtonLabel(text = "Enabled")
      }
      SunsetButton(onClick = {}, enabled = false) {
        SunsetText.ButtonLabel(text = "Disabled")
      }
      SunsetTextButton.Label(text = "Text Button", onClick = {})
      SunsetTextButton.Label(text = "Text Disabled", onClick = {}, enabled = false)
    }
  }
}
