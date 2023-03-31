package com.mataku.scrobscrob.ui_common.molecule

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.ui_common.SunsetTextStyle

@Composable
fun Chip(
  name: String,
  isSelected: Boolean = false,
  toggleable: Boolean = true,
  onSelectionChanged: (String) -> Unit = {},
) {
  Surface(
    modifier = Modifier.padding(4.dp),
    shadowElevation = 8.dp,
    shape = MaterialTheme.shapes.medium,
    color = if (isSelected) Color.LightGray else MaterialTheme.colorScheme.primary
  ) {
    Row(modifier = Modifier
      .toggleable(
        value = isSelected,
        enabled = toggleable,
        onValueChange = {
          onSelectionChanged(name)
        }
      )
    ) {
      Text(
        text = name,
        style = SunsetTextStyle.body2,
        modifier = Modifier.padding(8.dp)
      )
    }
  }
}
