package com.mataku.scrobscrob.data.repository

import app.cash.turbine.test
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.endpoint.AuthSessionEndpoint
import com.mataku.scrobscrob.data.api.endpoint.Endpoint
import com.mataku.scrobscrob.data.api.model.AuthSessionApiResponse
import com.mataku.scrobscrob.data.api.model.SessionBody
import com.mataku.scrobscrob.data.db.ScrobbleAppDataStore
import com.mataku.scrobscrob.data.db.SessionBackupPayload
import com.mataku.scrobscrob.data.db.SessionBackupStore
import com.mataku.scrobscrob.data.db.SessionKeyDataStore
import com.mataku.scrobscrob.data.db.UsernameDataStore
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldEndWith
import io.kotest.matchers.string.shouldNotBeBlank
import io.kotest.matchers.string.shouldStartWith
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.flowOf

class SessionRepositorySpec : DescribeSpec({
  describe("authorize") {
    it("issues AuthSessionEndpoint with the token, persists session key and username, then emits Unit") {
      val token = "tok123"
      val username = "matakucom"
      val sessionKey = "abcdef0123456789"
      val service = mockk<LastFmService>()
      val sessionKeyDataStore = mockk<SessionKeyDataStore>()
      val usernameDataStore = mockk<UsernameDataStore>()
      val scrobbleAppDataStore = mockk<ScrobbleAppDataStore>()
      val sessionBackupStore = mockk<SessionBackupStore>()

      val slot = slot<Endpoint<*>>()
      val fakeResponse = AuthSessionApiResponse(
        session = SessionBody(
          name = username,
          key = sessionKey,
        ),
      )
      coEvery { service.rawRequest(capture(slot), any()) } returns fakeResponse
      coEvery { sessionKeyDataStore.setSessionKey(sessionKey) } returns flowOf(Unit)
      coEvery { usernameDataStore.setUsername(username) } returns flowOf(Unit)
      coEvery { sessionBackupStore.save(SessionBackupPayload(sessionKey = sessionKey, username = username)) } returns Unit

      val repository = SessionRepositoryImpl(
        service, sessionKeyDataStore, usernameDataStore, scrobbleAppDataStore, sessionBackupStore,
      )
      repository.authorize(token = token).test {
        awaitItem() shouldBe Unit
        awaitComplete()
      }

      coVerify(exactly = 1) { sessionKeyDataStore.setSessionKey(sessionKey) }
      coVerify(exactly = 1) { usernameDataStore.setUsername(username) }
      coVerify(exactly = 1) { sessionBackupStore.save(SessionBackupPayload(sessionKey = sessionKey, username = username)) }

      val captured = slot.captured
      captured.shouldBeInstanceOf<AuthSessionEndpoint>()
      captured.params["token"] shouldBe token
      captured.params["method"] shouldBe "auth.getSession"
      captured.params["api_key"].shouldBeInstanceOf<String>().shouldNotBeBlank()
      captured.params["api_sig"].shouldBeInstanceOf<String>().shouldNotBeBlank()
      captured.params.keys shouldBe setOf("token", "method", "api_key", "api_sig")
    }
  }

  describe("webAuthUrl") {
    it("emits the Last.fm web auth URL with the api key and the encoded callback") {
      val service = mockk<LastFmService>()
      val sessionKeyDataStore = mockk<SessionKeyDataStore>()
      val usernameDataStore = mockk<UsernameDataStore>()
      val scrobbleAppDataStore = mockk<ScrobbleAppDataStore>()
      val sessionBackupStore = mockk<SessionBackupStore>()

      val repository = SessionRepositoryImpl(
        service, sessionKeyDataStore, usernameDataStore, scrobbleAppDataStore, sessionBackupStore,
      )
      repository.webAuthUrl().test {
        val url = awaitItem()
        url shouldStartWith "https://www.last.fm/api/auth/?api_key="
        url shouldEndWith "&cb=https%3A%2F%2Fsunsetscrob.mataku.com%2Fauth%2Flastfm"
        awaitComplete()
      }
      coVerify(exactly = 0) { service.rawRequest(any(), any()) }
    }
  }

  describe("logout") {
    it("clears session key, username, and allowed apps then emits Unit") {
      val service = mockk<LastFmService>()
      val sessionKeyDataStore = mockk<SessionKeyDataStore>()
      val usernameDataStore = mockk<UsernameDataStore>()
      val scrobbleAppDataStore = mockk<ScrobbleAppDataStore>()
      val sessionBackupStore = mockk<SessionBackupStore>()
      coEvery { sessionKeyDataStore.remove() } returns Unit
      coEvery { usernameDataStore.remove() } returns Unit
      coEvery { scrobbleAppDataStore.clear() } returns Unit
      coEvery { sessionBackupStore.clear() } returns Unit

      val repository = SessionRepositoryImpl(
        service, sessionKeyDataStore, usernameDataStore, scrobbleAppDataStore, sessionBackupStore,
      )
      repository.logout().test {
        awaitItem() shouldBe Unit
        awaitComplete()
      }

      coVerify(exactly = 1) { sessionKeyDataStore.remove() }
      coVerify(exactly = 1) { usernameDataStore.remove() }
      coVerify(exactly = 1) { scrobbleAppDataStore.clear() }
      coVerify(exactly = 1) { sessionBackupStore.clear() }
      coVerify(exactly = 0) { service.rawRequest(any(), any()) }
    }
  }

  describe("recoverFromKeystoreLossIfNeeded") {
    it("clears all stores when sessionKey is null but username is present") {
      val service = mockk<LastFmService>()
      val sessionKeyDataStore = mockk<SessionKeyDataStore>()
      val usernameDataStore = mockk<UsernameDataStore>()
      val scrobbleAppDataStore = mockk<ScrobbleAppDataStore>()
      val sessionBackupStore = mockk<SessionBackupStore>()
      coEvery { sessionKeyDataStore.sessionKey() } returns null
      coEvery { usernameDataStore.username() } returns "matakucom"
      coEvery { sessionKeyDataStore.remove() } returns Unit
      coEvery { usernameDataStore.remove() } returns Unit
      coEvery { scrobbleAppDataStore.clear() } returns Unit

      val repository = SessionRepositoryImpl(
        service, sessionKeyDataStore, usernameDataStore, scrobbleAppDataStore, sessionBackupStore,
      )
      repository.recoverFromKeystoreLossIfNeeded().test {
        awaitItem() shouldBe Unit
        awaitComplete()
      }

      coVerify(exactly = 1) { sessionKeyDataStore.remove() }
      coVerify(exactly = 1) { usernameDataStore.remove() }
      coVerify(exactly = 1) { scrobbleAppDataStore.clear() }
    }

    it("does not touch any store when both sessionKey and username are absent (fresh install)") {
      val service = mockk<LastFmService>()
      val sessionKeyDataStore = mockk<SessionKeyDataStore>()
      val usernameDataStore = mockk<UsernameDataStore>()
      val scrobbleAppDataStore = mockk<ScrobbleAppDataStore>()
      val sessionBackupStore = mockk<SessionBackupStore>()
      coEvery { sessionKeyDataStore.sessionKey() } returns null
      coEvery { usernameDataStore.username() } returns null

      val repository = SessionRepositoryImpl(
        service, sessionKeyDataStore, usernameDataStore, scrobbleAppDataStore, sessionBackupStore,
      )
      repository.recoverFromKeystoreLossIfNeeded().test {
        awaitItem() shouldBe Unit
        awaitComplete()
      }

      coVerify(exactly = 0) { sessionKeyDataStore.remove() }
      coVerify(exactly = 0) { usernameDataStore.remove() }
      coVerify(exactly = 0) { scrobbleAppDataStore.clear() }
    }

    it("does not touch any store when the user is logged in normally") {
      val service = mockk<LastFmService>()
      val sessionKeyDataStore = mockk<SessionKeyDataStore>()
      val usernameDataStore = mockk<UsernameDataStore>()
      val scrobbleAppDataStore = mockk<ScrobbleAppDataStore>()
      val sessionBackupStore = mockk<SessionBackupStore>()
      coEvery { sessionKeyDataStore.sessionKey() } returns "abcdef0123456789"
      coEvery { usernameDataStore.username() } returns "matakucom"

      val repository = SessionRepositoryImpl(
        service, sessionKeyDataStore, usernameDataStore, scrobbleAppDataStore, sessionBackupStore,
      )
      repository.recoverFromKeystoreLossIfNeeded().test {
        awaitItem() shouldBe Unit
        awaitComplete()
      }

      coVerify(exactly = 0) { sessionKeyDataStore.remove() }
      coVerify(exactly = 0) { usernameDataStore.remove() }
      coVerify(exactly = 0) { scrobbleAppDataStore.clear() }
    }
  }

  describe("restoreSessionFromBackupIfNeeded") {
    it("restores session key and username from backup when no local session exists") {
      val service = mockk<LastFmService>()
      val sessionKeyDataStore = mockk<SessionKeyDataStore>()
      val usernameDataStore = mockk<UsernameDataStore>()
      val scrobbleAppDataStore = mockk<ScrobbleAppDataStore>()
      val sessionBackupStore = mockk<SessionBackupStore>()
      coEvery { sessionKeyDataStore.sessionKey() } returns null
      coEvery { sessionBackupStore.restore() } returns SessionBackupPayload(
        sessionKey = "abcdef0123456789",
        username = "matakucom",
      )
      coEvery { sessionKeyDataStore.setSessionKey("abcdef0123456789") } returns flowOf(Unit)
      coEvery { usernameDataStore.setUsername("matakucom") } returns flowOf(Unit)

      val repository = SessionRepositoryImpl(
        service, sessionKeyDataStore, usernameDataStore, scrobbleAppDataStore, sessionBackupStore,
      )
      repository.restoreSessionFromBackupIfNeeded().test {
        awaitItem() shouldBe Unit
        awaitComplete()
      }

      coVerify(exactly = 1) { sessionKeyDataStore.setSessionKey("abcdef0123456789") }
      coVerify(exactly = 1) { usernameDataStore.setUsername("matakucom") }
    }

    it("does not touch Block Store when a local session exists") {
      val service = mockk<LastFmService>()
      val sessionKeyDataStore = mockk<SessionKeyDataStore>()
      val usernameDataStore = mockk<UsernameDataStore>()
      val scrobbleAppDataStore = mockk<ScrobbleAppDataStore>()
      val sessionBackupStore = mockk<SessionBackupStore>()
      coEvery { sessionKeyDataStore.sessionKey() } returns "abcdef0123456789"

      val repository = SessionRepositoryImpl(
        service, sessionKeyDataStore, usernameDataStore, scrobbleAppDataStore, sessionBackupStore,
      )
      repository.restoreSessionFromBackupIfNeeded().test {
        awaitItem() shouldBe Unit
        awaitComplete()
      }

      coVerify(exactly = 0) { sessionBackupStore.restore() }
      coVerify(exactly = 0) { sessionKeyDataStore.setSessionKey(any()) }
      coVerify(exactly = 0) { usernameDataStore.setUsername(any()) }
    }

    it("writes nothing when the backup holds no payload") {
      val service = mockk<LastFmService>()
      val sessionKeyDataStore = mockk<SessionKeyDataStore>()
      val usernameDataStore = mockk<UsernameDataStore>()
      val scrobbleAppDataStore = mockk<ScrobbleAppDataStore>()
      val sessionBackupStore = mockk<SessionBackupStore>()
      coEvery { sessionKeyDataStore.sessionKey() } returns null
      coEvery { sessionBackupStore.restore() } returns null

      val repository = SessionRepositoryImpl(
        service, sessionKeyDataStore, usernameDataStore, scrobbleAppDataStore, sessionBackupStore,
      )
      repository.restoreSessionFromBackupIfNeeded().test {
        awaitItem() shouldBe Unit
        awaitComplete()
      }

      coVerify(exactly = 0) { sessionKeyDataStore.setSessionKey(any()) }
      coVerify(exactly = 0) { usernameDataStore.setUsername(any()) }
    }
  }
})
