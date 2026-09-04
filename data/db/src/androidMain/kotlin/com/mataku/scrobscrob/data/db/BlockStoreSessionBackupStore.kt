package com.mataku.scrobscrob.data.db

import android.content.Context
import com.google.android.gms.auth.blockstore.Blockstore
import com.google.android.gms.auth.blockstore.DeleteBytesRequest
import com.google.android.gms.auth.blockstore.RetrieveBytesRequest
import com.google.android.gms.auth.blockstore.StoreBytesData
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull

private const val BLOCK_STORE_KEY = "com.mataku.scrobscrob.session"
private const val GMS_TIMEOUT_MILLIS = 5_000L

internal class BlockStoreSessionBackupStore(
  context: Context,
) : SessionBackupStore {
  private val client = Blockstore.getClient(context)

  override suspend fun save(payload: SessionBackupPayload) {
    runCatching {
      withTimeoutOrNull(GMS_TIMEOUT_MILLIS) {
        val data = StoreBytesData.Builder()
          .setBytes(payload.toJsonBytes())
          .setKey(BLOCK_STORE_KEY)
          .setShouldBackupToCloud(true)
          .build()
        client.storeBytes(data).await()
      }
    }.onFailure { if (it is CancellationException) throw it }
  }

  override suspend fun restore(): SessionBackupPayload? = runCatching {
    withTimeoutOrNull(GMS_TIMEOUT_MILLIS) {
      val request = RetrieveBytesRequest.Builder()
        .setKeys(listOf(BLOCK_STORE_KEY))
        .build()
      val response = client.retrieveBytes(request).await()
      response.blockstoreDataMap[BLOCK_STORE_KEY]?.bytes?.let(SessionBackupPayload::fromJsonBytes)
    }
  }.onFailure { if (it is CancellationException) throw it }.getOrNull()

  override suspend fun clear() {
    runCatching {
      withTimeoutOrNull(GMS_TIMEOUT_MILLIS) {
        val request = DeleteBytesRequest.Builder()
          .setKeys(listOf(BLOCK_STORE_KEY))
          .build()
        client.deleteBytes(request).await()
      }
    }.onFailure { if (it is CancellationException) throw it }
  }
}
