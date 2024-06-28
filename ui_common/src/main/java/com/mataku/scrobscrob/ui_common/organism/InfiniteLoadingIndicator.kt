package com.mataku.scrobscrob.ui_common.organism

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.ui_common.molecule.LoadingIndicator

@Composable
fun InfiniteLoadingIndicator(
  onScrollEnd: () -> Unit,
  modifier: Modifier = Modifier,
  padding: Dp = 0.dp
) {
  CircularProgressIndicator(
    modifier = modifier
      .size(40.dp)
      .padding(horizontal = padding, vertical = 16.dp),
  )

  LaunchedEffect(key1 = Unit, block = {
    onScrollEnd()
  })
}

@Composable
fun InfiniteLoadingIndicator(
  onScrollEnd: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier,
    contentAlignment = Alignment.Center
  ) {
    LoadingIndicator(modifier = Modifier)
  }

  LaunchedEffect(key1 = Unit, block = {
    onScrollEnd()
  })
}
