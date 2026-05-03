package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.data.db.UsernameDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.SingleIn

interface UsernameRepository {
  fun asyncUsername(): Flow<String?>

  fun usernameFlow(): Flow<String?>
}

@SingleIn(AppScope::class)
@Inject
class UsernameRepositoryImpl(
  private val usernameDataStore: UsernameDataStore
) : UsernameRepository {
  override fun asyncUsername(): Flow<String?> = flow {
    emit(usernameDataStore.username())
  }.flowOn(Dispatchers.IO)

  override fun usernameFlow(): Flow<String?> = usernameDataStore.usernameFlow()
}
