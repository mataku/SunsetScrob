package com.mataku.scrobscrob.data.db

import app.cash.turbine.test
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first

class ScrobbleAppDataStoreSpec : DescribeSpec({
  describe("ScrobbleAppDataStore") {
    it("returns an empty set when nothing has been stored") {
      withTestPreferencesDataStore { dataStore ->
        val store = ScrobbleAppDataStore(dataStore)

        store.allowedApps().shouldBeEmpty()
        store.allowedAppsFlow().first().shouldBeEmpty()
      }
    }

    it("accumulates apps passed to allowApp") {
      withTestPreferencesDataStore { dataStore ->
        val store = ScrobbleAppDataStore(dataStore)
        store.allowApp("com.apple.android.music").collect()
        store.allowApp("com.spotify.music").collect()

        store.allowedApps() shouldBe setOf("com.apple.android.music", "com.spotify.music")
      }
    }

    it("removes only the app passed to disallowApp") {
      withTestPreferencesDataStore { dataStore ->
        val store = ScrobbleAppDataStore(dataStore)
        store.allowApp("com.apple.android.music").collect()
        store.allowApp("com.spotify.music").collect()

        store.disallowApp("com.apple.android.music").collect()

        store.allowedApps() shouldBe setOf("com.spotify.music")
      }
    }

    it("emits each change to allowedAppsFlow collectors") {
      withTestPreferencesDataStore { dataStore ->
        val store = ScrobbleAppDataStore(dataStore)

        store.allowedAppsFlow().test {
          awaitItem().shouldBeEmpty()
          store.allowApp("com.spotify.music").collect()
          awaitItem() shouldBe setOf("com.spotify.music")
          store.disallowApp("com.spotify.music").collect()
          awaitItem().shouldBeEmpty()
          cancelAndIgnoreRemainingEvents()
        }
      }
    }

    it("clears every allowed app on clear") {
      withTestPreferencesDataStore { dataStore ->
        val store = ScrobbleAppDataStore(dataStore)
        store.allowApp("com.spotify.music").collect()

        store.clear()

        store.allowedApps().shouldBeEmpty()
      }
    }
  }
})
