package com.mataku.scrobscrob.app.smoke.di

import android.content.Context
import com.mataku.scrobscrob.app.smoke.SmokeScope
import com.mataku.scrobscrob.app.smoke.fixtures.FixtureDispatcher
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine

@BindingContainer
@ContributesTo(SmokeScope::class)
interface MockApiModule {
  companion object {
    @SingleIn(AppScope::class)
    @Provides
    fun provideHttpClientEngine(context: Context): HttpClientEngine {
      val dispatcher = FixtureDispatcher(context.assets)
      return MockEngine { request ->
        with(dispatcher) { dispatch(request) }
      }
    }
  }
}
