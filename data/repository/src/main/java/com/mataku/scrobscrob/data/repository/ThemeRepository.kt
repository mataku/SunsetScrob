package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.data.db.ThemeDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

interface ThemeRepository {
  suspend fun currentTheme(): Flow<AppTheme>
  suspend fun storeTheme(theme: AppTheme): Flow<Unit>
}

@Singleton
class ThemeRepositoryImpl @Inject constructor(
  private val themeDataStore: ThemeDataStore
) : ThemeRepository {
  override suspend fun currentTheme(): Flow<AppTheme> {
    return themeDataStore.theme().flowOn(Dispatchers.IO)
  }

  override suspend fun storeTheme(theme: AppTheme): Flow<Unit> =
    themeDataStore.setTheme(theme)
}
