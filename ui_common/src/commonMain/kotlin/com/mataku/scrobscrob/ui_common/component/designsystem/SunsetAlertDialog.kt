package com.mataku.scrobscrob.ui_common.component.designsystem

import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import com.mataku.scrobscrob.ui_common.style.onSurfaceColor

@Composable
fun SunsetAlertDialog(
  title: String,
  confirmButtonText: String,
  onConfirmButton: () -> Unit,
  modifier: Modifier = Modifier,
  description: String = "",
  onDismissRequest: () -> Unit = {},
  onDismissButton: () -> Unit = {},
  dismissButtonText: String = "",
) {
  AlertDialog(
    modifier = modifier,
    onDismissRequest = onDismissRequest,
    title = if (title.isNotBlank()) {
      { SunsetText.Body(text = title) }
    } else null,
    text = if (description.isNotBlank()) {
      { SunsetText.Body(text = description) }
    } else null,
    confirmButton = {
      SunsetTextButton(onClick = {
        onConfirmButton.invoke()
      }) {
        SunsetText.Body(
          text = confirmButtonText,
          color = LocalAppTheme.current.onSurfaceColor(),
        )
      }
    },
    dismissButton = if (dismissButtonText.isNotBlank()) {
      {
        SunsetTextButton(onClick = {
          onDismissButton.invoke()
        }) {
          SunsetText.Body(
            text = dismissButtonText,
            color = LocalAppTheme.current.onSurfaceColor(),
          )
        }
      }
    } else null
  )
}
