package com.mataku.scrobscrob.ui_common.molecule

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material3.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import com.mataku.scrobscrob.ui_common.style.accentColor

@Composable
fun FilteringFloatingButton(
  onClick: () -> Unit,
  modifier: Modifier
) {
  FloatingActionButton(
    onClick = onClick,
    containerColor = LocalAppTheme.current.accentColor(),
    modifier = modifier,
    shape = FloatingActionButtonDefaults.shape
  ) {
    Icon(
      painter = rememberVectorPainter(image = Icons.Filled.FilterList),
      contentDescription = "filter content"
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun FilteringFloatingButtonPreview() {
  SunsetThemePreview {
    Surface {
      Box(
        modifier = Modifier
          .wrapContentSize()
          .padding(16.dp)
      ) {
        FilteringFloatingButton(
          onClick = {},
          modifier = Modifier
        )
      }
    }
  }
}
