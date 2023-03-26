package com.mataku.scrobscrob.artist.di

import com.mataku.scrobscrob.artist.ui.viewmodel.TopArtistsViewModel
import com.mataku.scrobscrob.data.repository.di.repositoryModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val artistModule = module {
  includes(repositoryModule)
  viewModel {
    TopArtistsViewModel(
      get(),
      get()
    )
  }
}
