package com.mataku.scrobscrob.app.di

import android.app.Application
import android.content.Context
import coil3.ImageLoader
import com.mataku.scrobscrob.data.repository.di.ScrobbleServiceDependencies
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metrox.android.MetroAppComponentProviders
import dev.zacsweers.metrox.viewmodel.ViewModelGraph

internal interface AppGraphContract :
  MetroAppComponentProviders,
  ViewModelGraph,
  ScrobbleServiceDependencies {

  val imageLoader: ImageLoader
}

@DependencyGraph(AppScope::class)
internal interface AppGraph : AppGraphContract {

  @Provides
  fun provideApplicationContext(application: Application): Context = application

  @DependencyGraph.Factory
  fun interface Factory {
    fun create(@Provides application: Application): AppGraph
  }
}
