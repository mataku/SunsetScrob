package com.mataku.scrobscrob.album.di

import com.mataku.scrobscrob.album.ui.viewmodel.TopAlbumsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val albumModule = module {
  viewModel {
    TopAlbumsViewModel(
      get(),
      get()
    )
  }
}
