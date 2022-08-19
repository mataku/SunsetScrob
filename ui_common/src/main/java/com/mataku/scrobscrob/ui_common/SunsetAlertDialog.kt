package com.mataku.scrobscrob.ui_common

import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable

@Composable
fun SunsetAlertDialog(
  title: String,
  description: String = "",
  onDismissRequest: () -> Unit = {},
  onConfirmButton: () -> Unit,
  onDismissButton: () -> Unit = {},
  dismissButtonText: String = "",
  confirmButtonText: String
) {
  AlertDialog(
    onDismissRequest = onDismissRequest,
    title = if (title.isNotBlank()) {
      { Text(text = title) }
    } else null,
    text = if (description.isNotBlank()) {
      { Text(text = description) }
    } else null,
    confirmButton = {
      TextButton(onClick = {
        onConfirmButton.invoke()
      }) {
        Text(
          text = confirmButtonText, style = SunsetTextStyle.body1.copy(
            MaterialTheme.colors.onSurface
          )
        )
      }
    },
    dismissButton = if (dismissButtonText.isNotBlank()) {
      {
        TextButton(onClick = {
          onDismissButton.invoke()
        }) {
          Text(
            text = dismissButtonText, style = SunsetTextStyle.body1.copy(
              MaterialTheme.colors.onSurface
            )
          )
        }
      }
    } else null
  )
}
