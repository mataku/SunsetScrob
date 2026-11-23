package com.mataku.scrobscrob.data.db

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import java.io.File
import java.nio.file.Files

internal suspend fun withTestPreferencesDataStore(block: suspend (DataStore<Preferences>) -> Unit) {
  val tempDir = Files.createTempDirectory("preferences-spec").toFile()
  val file = File(tempDir, "test.preferences_pb")
  val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
  val dataStore = PreferenceDataStoreFactory.create(
    scope = scope,
    produceFile = { file },
  )
  try {
    block(dataStore)
  } finally {
    scope.cancel()
    file.delete()
    tempDir.delete()
  }
}
