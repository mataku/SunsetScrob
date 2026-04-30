package com.mataku.scrobscrob.ui_common.navigation

import androidx.compose.runtime.Composable
import kotlin.reflect.KClass

class SunsetNavBuilder internal constructor(
  @PublishedApi
  internal val handlers:
    MutableMap<KClass<out SunsetNavKey>, @Composable SunsetDestinationScope.(SunsetNavKey) -> Unit>,
  private val onNavigate: (SunsetNavKey) -> Unit,
  private val onPopBackStack: () -> Unit,
) {
  inline fun <reified K : SunsetNavKey> destination(
    noinline content: @Composable SunsetDestinationScope.(K) -> Unit,
  ) {
    @Suppress("UNCHECKED_CAST")
    handlers[K::class] = content as @Composable SunsetDestinationScope.(SunsetNavKey) -> Unit
  }

  fun navigate(key: SunsetNavKey) = onNavigate(key)
  fun popBackStack() = onPopBackStack()
}
