package com.mataku.scrobscrob.ui_common.style

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun isCompactWidth(): Boolean {
  return calculatePaneScaffoldDirective(currentWindowAdaptiveInfo()).maxHorizontalPartitions == 1
}
