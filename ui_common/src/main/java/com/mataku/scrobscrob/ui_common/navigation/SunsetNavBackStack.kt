package com.mataku.scrobscrob.ui_common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList

@Stable
class SunsetNavBackStack internal constructor(initial: SunsetNavKey) {
  internal val entries: SnapshotStateList<SunsetNavEntry> =
    mutableStateListOf(SunsetNavEntry(initial))
  fun isEmpty(): Boolean = entries.isEmpty()
}

@Composable
fun rememberSunsetNavBackStack(initial: SunsetNavKey): SunsetNavBackStack =
  rememberSaveable(saver = SunsetNavBackStackSaver) {
    SunsetNavBackStack(initial)
  }

internal val SunsetNavBackStackSaver = listSaver<SunsetNavBackStack, SunsetNavKey>(
  save = { backStack -> backStack.entries.map(SunsetNavEntry::key) },
  restore = { saved ->
    val first = saved.firstOrNull() ?: error("empty backstack saved state")
    SunsetNavBackStack(first).also { stack ->
      saved.drop(1).forEach { stack.entries.add(SunsetNavEntry(it)) }
    }
  },
)
