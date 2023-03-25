package com.mataku.scrobscrob.account.di

import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.mataku.scrobscrob.account.ui.viewmodel.AccountViewModel
import com.mataku.scrobscrob.account.ui.viewmodel.ScrobbleSettingViewModel
import com.mataku.scrobscrob.account.ui.viewmodel.ThemeSelectorViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val accountModule = module {
  single<AppUpdateManager> {
    AppUpdateManagerFactory.create(get())
  }

  viewModel {
    AccountViewModel(
      get(),
      get(),
      get(),
      get()
    )
  }

  viewModel {
    ScrobbleSettingViewModel(
      get()
    )
  }

  viewModel {
    ThemeSelectorViewModel(
      get()
    )
  }
}
