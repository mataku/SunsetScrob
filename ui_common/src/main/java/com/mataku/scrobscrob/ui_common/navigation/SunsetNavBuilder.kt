package com.mataku.scrobscrob.ui_common.navigation

import androidx.compose.runtime.Composable
import kotlin.reflect.KClass

class SunsetNavBuilder internal constructor(
  internal val handlers:
    MutableMap<KClass<out SunsetNavKey>, @Composable SunsetDestinationScope.(SunsetNavKey) -> Unit>,
  private val onNavigate: (SunsetNavKey) -> Unit,
  private val onPopBackStack: () -> Unit,
) {
  inline fun <reified K : SunsetNavKey> destination(
    noinline content: @Composable SunsetDestinationScope.(K) -> Unit,
  ) {
    registerDestination(K::class, content)
  }

  @PublishedApi
  internal fun <K : SunsetNavKey> registerDestination(
    type: KClass<K>,
    content: @Composable SunsetDestinationScope.(K) -> Unit,
  ) {
    handlers[type] = { key ->
      @Suppress("UNCHECKED_CAST")
      content(key as K)
    }
  }

  fun navigate(key: SunsetNavKey) = onNavigate(key)
  fun popBackStack() = onPopBackStack()
}
