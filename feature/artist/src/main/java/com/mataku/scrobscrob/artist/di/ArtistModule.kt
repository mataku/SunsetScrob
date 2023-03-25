package com.mataku.scrobscrob.artist.di

import com.mataku.scrobscrob.artist.ui.viewmodel.TopArtistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val artistModule = module {
  viewModel {
    TopArtistsViewModel(
      get(),
      get()
    )
  }
}
