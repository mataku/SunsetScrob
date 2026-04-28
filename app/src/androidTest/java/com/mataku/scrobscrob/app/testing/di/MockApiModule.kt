package com.mataku.scrobscrob.app.testing.di

import androidx.test.platform.app.InstrumentationRegistry
import com.mataku.scrobscrob.app.testing.fixtures.FixtureDispatcher
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine

@ContributesTo(AppScope::class)
interface MockApiModule {
  companion object {
    @SingleIn(AppScope::class)
    @Provides
    fun provideHttpClientEngine(): HttpClientEngine {
      val dispatcher = FixtureDispatcher(
        InstrumentationRegistry.getInstrumentation().context.assets
      )
      return MockEngine { request ->
        with(dispatcher) { dispatch(request) }
      }
    }
  }
}
