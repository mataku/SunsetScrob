package com.mataku.scrobscrob.account.di

import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.mataku.scrobscrob.account.ui.viewmodel.AccountViewModel
import com.mataku.scrobscrob.account.ui.viewmodel.ScrobbleSettingViewModel
import com.mataku.scrobscrob.account.ui.viewmodel.ThemeSelectorViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AccountModule {
  @Singleton
  @Provides
  fun provideAppUpdateManager(@ApplicationContext context: Context): AppUpdateManager {
    return AppUpdateManagerFactory.create(context)
  }
}

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
