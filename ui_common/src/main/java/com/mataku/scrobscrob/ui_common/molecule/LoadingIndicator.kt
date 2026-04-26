package com.mataku.scrobscrob.ui_common.molecule

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.ui_common.SunsetCircularProgressIndicator

@Composable
fun LoadingIndicator(
  modifier: Modifier = Modifier
) {
  SunsetCircularProgressIndicator(
    modifier = modifier
      .size(40.dp)
  )
}
