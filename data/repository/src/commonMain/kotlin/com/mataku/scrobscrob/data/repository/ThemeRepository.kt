package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.data.db.ThemeDataStore
import com.mataku.scrobscrob.data.repository.mapper.toAppTheme
import com.mataku.scrobscrob.data.repository.mapper.toAppThemeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.SingleIn

interface ThemeRepository {
  suspend fun currentTheme(): Flow<AppTheme>
  suspend fun storeTheme(theme: AppTheme): Flow<Unit>
}

@SingleIn(AppScope::class)
@Inject
class ThemeRepositoryImpl(
  private val themeDataStore: ThemeDataStore
) : ThemeRepository {
  override suspend fun currentTheme(): Flow<AppTheme> {
    return themeDataStore.theme().map {
      it.toAppTheme()
    }.flowOn(Dispatchers.IO)
  }

  override suspend fun storeTheme(theme: AppTheme): Flow<Unit> =
    themeDataStore.setTheme(theme.toAppThemeEntity())
}
