package com.mataku.scrobscrob.ui_common

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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
  @SuppressLint("ComposeNamingUppercase")
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
  ) {
    invoke(
      onClick = onClick,
      modifier = modifier,
      enabled = enabled,
    ) {
      SunsetText.ButtonLabel(text = text)
    }
  }
}
