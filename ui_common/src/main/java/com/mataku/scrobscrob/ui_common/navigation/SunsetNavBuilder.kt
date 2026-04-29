package com.mataku.scrobscrob.ui_common.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.EntryProviderScope

class SunsetNavBuilder internal constructor(
  internal val entryProviderScope: EntryProviderScope<SunsetNavKey>,
  private val onNavigate: (SunsetNavKey) -> Unit,
  private val onPopBackStack: () -> Unit,
  private val onReplaceTop: (SunsetNavKey) -> Unit,
) {
  inline fun <reified K : SunsetNavKey> destination(
    noinline content: @Composable SunsetDestinationScope.(K) -> Unit,
  ) {
    destinationInternal(K::class.java, content)
  }

  @PublishedApi
  internal fun <K : SunsetNavKey> destinationInternal(
    type: Class<K>,
    content: @Composable SunsetDestinationScope.(K) -> Unit,
  ) {
    SunsetEntryRegistrar.register(entryProviderScope, type, content)
  }

  fun navigate(key: SunsetNavKey) = onNavigate(key)
  fun popBackStack() = onPopBackStack()
  fun replaceTop(key: SunsetNavKey) = onReplaceTop(key)
}
