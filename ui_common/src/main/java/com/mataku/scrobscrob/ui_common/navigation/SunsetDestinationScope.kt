package com.mataku.scrobscrob.ui_common.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel

interface SunsetDestinationScope : SharedTransitionScope {
  val animatedContentScope: AnimatedContentScope
  fun navigate(key: SunsetNavKey)
  fun popBackStack()

  @Composable
  fun <VM : ViewModel> viewModelFor(key: SunsetNavKey, type: Class<VM>): VM
}

@Composable
inline fun <reified VM : ViewModel> SunsetDestinationScope.viewModelFor(key: SunsetNavKey): VM =
  viewModelFor(key = key, type = VM::class.java)