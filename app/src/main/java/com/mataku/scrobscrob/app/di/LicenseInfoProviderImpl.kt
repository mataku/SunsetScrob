package com.mataku.scrobscrob.app.di

import android.content.Context
import com.mataku.scrobscrob.data.repository.di.LicenseInfoProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LicenseInfoProviderImpl @Inject constructor(
  @ApplicationContext
  private val context: Context
) : LicenseInfoProvider {
  override suspend fun licenseRawString(): String? {
    return withContext(Dispatchers.IO) {
      runCatching {
        val inputStream = context.assets.open("artifacts.json")
        inputStream.bufferedReader(charset = Charsets.UTF_8).use {
          it.readText()
        }
      }.getOrNull()
    }
  }
}
