package com.mataku.scrobscrob.ui_common.component

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoadingIndicator(
  modifier: Modifier = Modifier
) {
  SunsetCircularProgressIndicator(
    modifier = modifier
      .size(40.dp)
  )
}
