package com.mataku.scrobscrob.ui_common.organism

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.ui_common.molecule.LoadingIndicator

@Composable
fun InfiniteLoadingIndicator(onScrollEnd: () -> Unit, padding: Dp = 0.dp) {
  CircularProgressIndicator(
    modifier = Modifier
      .size(40.dp)
      .padding(horizontal = padding, vertical = 16.dp),
  )

  LaunchedEffect(key1 = true, block = {
    onScrollEnd()
  })
}

@Composable
fun InfiniteLoadingIndicator(onScrollEnd: () -> Unit, modifier: Modifier) {
  LoadingIndicator(modifier = modifier)

  LaunchedEffect(key1 = true, block = {
    onScrollEnd()
  })
}
