package com.mataku.scrobscrob.album.di

import com.mataku.scrobscrob.album.ui.viewmodel.TopAlbumsViewModel
import com.mataku.scrobscrob.data.repository.di.repositoryModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val albumModule = module {
  includes(repositoryModule)
  viewModel {
    TopAlbumsViewModel(
      get(),
      get()
    )
  }
}
