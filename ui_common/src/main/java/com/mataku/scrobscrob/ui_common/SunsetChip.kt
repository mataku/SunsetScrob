package com.mataku.scrobscrob.ui_common

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape

@Composable
fun SunsetChip(
  onClick: () -> Unit,
  label: @Composable () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  shape: Shape = ShapeDefaults.Large,
  border: BorderStroke? = SunsetChipDefaults.border(enabled = enabled),
) {
  SuggestionChip(
    onClick = onClick,
    label = label,
    modifier = modifier,
    enabled = enabled,
    shape = shape,
    border = border,
  )
}

object SunsetChipDefaults {
  @Composable
  fun border(enabled: Boolean = true): BorderStroke? = SuggestionChipDefaults.suggestionChipBorder(
    enabled = enabled,
    borderColor = MaterialTheme.colorScheme.onSecondary,
  )
}
