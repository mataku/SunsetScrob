package com.mataku.scrobscrob.app.di

import com.mataku.scrobscrob.account.AppInfoProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.koin.dsl.module

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
  @Binds
  abstract fun provideAppInfoProvider(appInfoProviderImpl: AppInfoProviderImpl): AppInfoProvider
}

val appModule = module {
  single<AppInfoProvider> {
    AppInfoProviderImpl()
  }
}
