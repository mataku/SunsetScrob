package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.data.db.ScrobbleAppDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

interface ScrobbleSettingRepository {
  suspend fun allowedApps(): Set<String>

  suspend fun allowedAppsFlow(): Flow<Set<String>>

  suspend fun allowApp(appName: String): Flow<Unit>

  suspend fun disallowApp(appName: String): Flow<Unit>
}

class ScrobbleSettingRepositoryImpl(
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

  override suspend fun allowedAppsFlow(): Flow<Set<String>> =
    scrobbleAppDataStore.allowedAppsFlow().distinctUntilChanged().flowOn(Dispatchers.IO)

  override suspend fun disallowApp(appName: String): Flow<Unit> =
    scrobbleAppDataStore.disallowApp(appName)

}
