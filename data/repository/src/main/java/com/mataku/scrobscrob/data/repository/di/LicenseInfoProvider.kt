package com.mataku.scrobscrob.data.repository.di

interface LicenseInfoProvider {
  suspend fun licenseRawString(): String?
}
