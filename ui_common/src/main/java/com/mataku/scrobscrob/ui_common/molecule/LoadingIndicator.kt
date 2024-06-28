package com.mataku.scrobscrob.ui_common.molecule

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoadingIndicator(
  modifier: Modifier = Modifier
) {
  CircularProgressIndicator(
    modifier = modifier
      .size(40.dp)
  )
}
