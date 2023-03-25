package com.mataku.scrobscrob.app.di

import com.mataku.scrobscrob.account.AppInfoProvider
import com.mataku.scrobscrob.app.ui.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
  single<AppInfoProvider> {
    AppInfoProviderImpl()
  }

  viewModel {
    MainViewModel(
      get(),
      get()
    )
  }
}
