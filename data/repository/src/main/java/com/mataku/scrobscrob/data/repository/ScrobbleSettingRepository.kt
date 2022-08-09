package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.data.db.ScrobbleAppDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

interface ScrobbleSettingRepository {
  suspend fun allowedApps(): Set<String>

  suspend fun allowApp(appName: String): Flow<Unit>
}

@Singleton
class ScrobbleSettingRepositoryImpl @Inject constructor(
  private val scrobbleAppDataStore: ScrobbleAppDataStore
) : ScrobbleSettingRepository {
  override suspend fun allowApp(appName: String): Flow<Unit> {
    return scrobbleAppDataStore.allowApp(appName).flowOn(Dispatchers.IO)
      .catch {

      }.map { }
  }

  override suspend fun allowedApps(): Set<String> {
    return scrobbleAppDataStore.allowedApps()
  }
}
