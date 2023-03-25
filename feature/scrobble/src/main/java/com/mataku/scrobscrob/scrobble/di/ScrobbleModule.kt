package com.mataku.scrobscrob.scrobble.di

import com.mataku.scrobscrob.scrobble.ui.viewmodel.ScrobbleViewModel
import com.mataku.scrobscrob.scrobble.ui.viewmodel.TrackViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val scrobbleModule = module {
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
