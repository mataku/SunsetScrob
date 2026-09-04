package com.mataku.scrobscrob.data.repository

import app.cash.turbine.test
import com.mataku.scrobscrob.data.db.ScrobbleAppDataStore
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class ScrobbleSettingRepositorySpec : DescribeSpec({
  describe("allowedAppsFlow") {
    it("forwards the data store's distinct allowed-apps flow") {
      val dataStore = mockk<ScrobbleAppDataStore>()
      val allowed = setOf("com.spotify.music", "com.apple.music")
      coEvery { dataStore.allowedAppsFlow() } returns flowOf(allowed, allowed, setOf("com.apple.music"))

      val repository = ScrobbleSettingRepositoryImpl(dataStore)

      repository.allowedAppsFlow().test {
        awaitItem() shouldBe allowed
        awaitItem() shouldBe setOf("com.apple.music")
        awaitComplete()
      }
    }
  }

  describe("allowApp") {
    it("delegates to ScrobbleAppDataStore.allowApp and emits Unit") {
      val dataStore = mockk<ScrobbleAppDataStore>()
      val appName = "com.spotify.music"
      coEvery { dataStore.allowApp(appName) } returns flowOf(Unit)

      val repository = ScrobbleSettingRepositoryImpl(dataStore)

      repository.allowApp(appName).test {
        awaitItem() shouldBe Unit
        awaitComplete()
      }

      coVerify(exactly = 1) { dataStore.allowApp(appName) }
    }

    it("swallows errors from the data store and completes") {
      // The implementation deliberately catches and discards errors from
      // allowApp so a flaky DataStore write never bubbles up to the UI.
      val dataStore = mockk<ScrobbleAppDataStore>()
      val appName = "com.spotify.music"
      coEvery { dataStore.allowApp(appName) } returns flow { throw RuntimeException("boom") }

      val repository = ScrobbleSettingRepositoryImpl(dataStore)

      repository.allowApp(appName).test {
        awaitComplete()
      }
    }
  }

  describe("disallowApp") {
    it("delegates to ScrobbleAppDataStore.disallowApp and emits Unit") {
      val dataStore = mockk<ScrobbleAppDataStore>()
      val appName = "com.spotify.music"
      coEvery { dataStore.disallowApp(appName) } returns flowOf(Unit)

      val repository = ScrobbleSettingRepositoryImpl(dataStore)

      repository.disallowApp(appName).test {
        awaitItem() shouldBe Unit
        awaitComplete()
      }

      coVerify(exactly = 1) { dataStore.disallowApp(appName) }
    }
  }
})
