package com.mataku.scrobscrob.data.db

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import app.cash.turbine.test
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import java.io.File
import java.nio.file.Files

class SessionKeyDataStoreSpec : DescribeSpec({
  describe("SessionKeyDataStore") {
    it("returns null when nothing has been stored") {
      withTestDataStore { dataStore ->
        SessionKeyDataStore(dataStore).sessionKey().shouldBeNull()
      }
    }

    it("returns null when the stored value is empty") {
      withTestDataStore { dataStore ->
        dataStore.updateData { "" }
        SessionKeyDataStore(dataStore).sessionKey().shouldBeNull()
      }
    }

    it("persists the value passed to setSessionKey and exposes it via sessionKey") {
      withTestDataStore { dataStore ->
        val store = SessionKeyDataStore(dataStore)
        store.setSessionKey("token-123").test {
          awaitItem() shouldBe Unit
          awaitComplete()
        }
        store.sessionKey() shouldBe "token-123"
      }
    }

    it("overwrites the stored value when setSessionKey is called again") {
      withTestDataStore { dataStore ->
        val store = SessionKeyDataStore(dataStore)
        store.setSessionKey("first").test {
          awaitItem() shouldBe Unit
          awaitComplete()
        }
        store.setSessionKey("second").test {
          awaitItem() shouldBe Unit
          awaitComplete()
        }
        store.sessionKey() shouldBe "second"
      }
    }

    it("clears the stored value on remove") {
      withTestDataStore { dataStore ->
        val store = SessionKeyDataStore(dataStore)
        store.setSessionKey("token-123").test {
          awaitItem() shouldBe Unit
          awaitComplete()
        }

        store.remove()

        store.sessionKey().shouldBeNull()
      }
    }
  }
})

private suspend fun withTestDataStore(block: suspend (DataStore<String>) -> Unit) {
  val tempDir = Files.createTempDirectory("session-key-spec").toFile()
  val file = File(tempDir, "session_key.pb")
  val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
  val dataStore = DataStoreFactory.create(
    serializer = SessionKeySerializer,
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
