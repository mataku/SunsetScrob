package com.mataku.scrobscrob.data.db

import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.io.InputStream
import java.io.OutputStream

@SingleIn(AppScope::class)
class SessionKeyDataStore(
  private val dataStore: DataStore<String>
) {
  suspend fun sessionKey(): String? =
    dataStore.data.first().takeIf { it.isNotEmpty() }

  suspend fun setSessionKey(sessionKey: String): Flow<Unit> = flowOf(
    dataStore.updateData { sessionKey }
  ).map { }

  suspend fun remove() {
    dataStore.updateData { "" }
  }
}

internal object SessionKeySerializer : Serializer<String> {
  override val defaultValue: String = ""

  override suspend fun readFrom(input: InputStream): String =
    input.readBytes().decodeToString()

  override suspend fun writeTo(t: String, output: OutputStream) {
    output.write(t.encodeToByteArray())
  }
}
