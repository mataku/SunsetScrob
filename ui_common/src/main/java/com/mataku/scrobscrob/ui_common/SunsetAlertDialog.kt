package com.mataku.scrobscrob.ui_common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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
      TextButton(onClick = {
        onConfirmButton.invoke()
      }) {
        SunsetText.Body(
          text = confirmButtonText,
          color = MaterialTheme.colorScheme.onSurface,
        )
      }
    },
    dismissButton = if (dismissButtonText.isNotBlank()) {
      {
        TextButton(onClick = {
          onDismissButton.invoke()
        }) {
          SunsetText.Body(
            text = dismissButtonText,
            color = MaterialTheme.colorScheme.onSurface,
          )
        }
      }
    } else null
  )
}
