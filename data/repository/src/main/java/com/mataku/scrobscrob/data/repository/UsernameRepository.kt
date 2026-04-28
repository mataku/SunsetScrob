package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.data.db.UsernameDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.SingleIn

interface UsernameRepository {
  // Synchronous accessor used by ViewModels that need the username during
  // construction (field initializers in TopAlbums/TopArtists/Account).
  // Migrating these call sites to a Flow requires restructuring those VMs;
  // tracked separately.
  @Suppress("RepositoryReturnsFlow")
  fun username(): String?

  fun asyncUsername(): Flow<String?>
}

@SingleIn(AppScope::class)
@Inject
class UsernameRepositoryImpl(
  private val usernameDataStore: UsernameDataStore
) : UsernameRepository {
  override fun username(): String? =
    runBlocking { usernameDataStore.username() }

  override fun asyncUsername(): Flow<String?> = flow {
    emit(usernameDataStore.username())
  }.flowOn(Dispatchers.IO)
}
