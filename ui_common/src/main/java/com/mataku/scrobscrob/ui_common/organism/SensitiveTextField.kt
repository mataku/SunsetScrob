package com.mataku.scrobscrob.ui_common.organism

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun SensitiveTextField(
  value: String,
  onValueChange: (String) -> Unit,
  imeAction: ImeAction = ImeAction.Done
) {
  BasicTextField(
    value = value,
    onValueChange = onValueChange,
    singleLine = true,
    keyboardOptions = KeyboardOptions(
      keyboardType = KeyboardType.Password,
      imeAction = imeAction
    )
  )
}
