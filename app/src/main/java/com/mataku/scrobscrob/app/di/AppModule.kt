package com.mataku.scrobscrob.app.di

import com.mataku.scrobscrob.account.AppInfoProvider
import com.mataku.scrobscrob.data.repository.di.LicenseInfoProvider
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
interface AppModule {
  @Binds
  fun provideAppInfoProvider(appInfoProviderImpl: AppInfoProviderImpl): AppInfoProvider

  @Binds
  fun provideLicenseInfoProvider(licenseInfoProviderImpl: LicenseInfoProviderImpl): LicenseInfoProvider
}
