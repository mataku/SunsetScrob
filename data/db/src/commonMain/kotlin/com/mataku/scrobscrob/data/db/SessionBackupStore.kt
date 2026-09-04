package com.mataku.scrobscrob.data.db

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

interface SessionBackupStore {
  suspend fun save(payload: SessionBackupPayload)
  suspend fun restore(): SessionBackupPayload?
  suspend fun clear()
}

@Serializable
data class SessionBackupPayload(
  val sessionKey: String,
  val username: String,
) {
  fun toJsonBytes(): ByteArray = Json.encodeToString(serializer(), this).encodeToByteArray()

  companion object {
    fun fromJsonBytes(bytes: ByteArray): SessionBackupPayload? = runCatching {
      Json.decodeFromString(serializer(), bytes.decodeToString())
    }.getOrNull()
  }
}
