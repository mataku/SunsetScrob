package com.mataku.scrobscrob.ui_common.navigation

import androidx.compose.runtime.Composable
import kotlin.reflect.KClass

class SunsetNavBuilder internal constructor(
  internal val handlers:
    MutableMap<KClass<out SunsetNavKey>, @Composable SunsetDestinationScope.(SunsetNavKey) -> Unit>,
  internal val transitionSpecs: MutableMap<KClass<out SunsetNavKey>, SunsetTransitionSpec>,
  private val onNavigate: (SunsetNavKey) -> Unit,
  private val onPopBackStack: () -> Unit,
) {
  inline fun <reified K : SunsetNavKey> destination(
    transitionSpec: SunsetTransitionSpec = SunsetTransitionSpec.Slide,
    noinline content: @Composable SunsetDestinationScope.(K) -> Unit,
  ) {
    registerDestination(K::class, transitionSpec, content)
  }

  @PublishedApi
  internal fun <K : SunsetNavKey> registerDestination(
    type: KClass<K>,
    transitionSpec: SunsetTransitionSpec,
    content: @Composable SunsetDestinationScope.(K) -> Unit,
  ) {
    handlers[type] = { key ->
      @Suppress("UNCHECKED_CAST")
      content(key as K)
    }
    transitionSpecs[type] = transitionSpec
  }

  fun navigate(key: SunsetNavKey) = onNavigate(key)
  fun popBackStack() = onPopBackStack()
}
