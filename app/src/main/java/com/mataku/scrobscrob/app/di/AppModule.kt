package com.mataku.scrobscrob.app.di

import com.mataku.scrobscrob.account.AppInfoProvider
import com.mataku.scrobscrob.app.ui.viewmodel.MainViewModel
import com.mataku.scrobscrob.data.repository.di.repositoryModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
  single<AppInfoProvider> {
    AppInfoProviderImpl()
  }

  includes(repositoryModule)

  viewModel {
    MainViewModel(
      get(),
      get()
    )
  }
}
