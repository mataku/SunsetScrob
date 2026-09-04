package com.mataku.scrobscrob.test_helper.unit

import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.Spec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class CoroutinesListener(
  private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestListener {

  override suspend fun beforeSpec(spec: Spec) {
    super.beforeSpec(spec)
    Dispatchers.setMain(testDispatcher)
  }

  override suspend fun afterSpec(spec: Spec) {
    super.afterSpec(spec)
    Dispatchers.resetMain()
  }
}
