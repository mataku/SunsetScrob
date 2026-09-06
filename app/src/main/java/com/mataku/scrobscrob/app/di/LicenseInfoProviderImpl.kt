package com.mataku.scrobscrob.app.di

import android.content.Context
import com.mataku.scrobscrob.data.repository.di.LicenseInfoProvider
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Inject
@SingleIn(AppScope::class)
class LicenseInfoProviderImpl(
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
