package com.mataku.scrobscrob.scrobble.di

import com.mataku.scrobscrob.data.repository.di.repositoryModule
import com.mataku.scrobscrob.scrobble.ui.viewmodel.ScrobbleViewModel
import com.mataku.scrobscrob.scrobble.ui.viewmodel.TrackViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val scrobbleModule = module {
  includes(repositoryModule)
  viewModel {
    ScrobbleViewModel(
      get()
    )
  }

  viewModel {
    TrackViewModel(
      get(),
      get()
    )
  }
}
