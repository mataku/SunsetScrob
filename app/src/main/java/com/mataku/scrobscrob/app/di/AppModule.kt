package com.mataku.scrobscrob.app.di

import com.mataku.scrobscrob.account.AppInfoProvider
import com.mataku.scrobscrob.data.repository.di.LicenseInfoProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
  @Binds
  abstract fun provideAppInfoProvider(appInfoProviderImpl: AppInfoProviderImpl): AppInfoProvider

  @Binds
  abstract fun provideLicenseInfoProvider(licenseInfoProviderImpl: LicenseInfoProviderImpl): LicenseInfoProvider
}
