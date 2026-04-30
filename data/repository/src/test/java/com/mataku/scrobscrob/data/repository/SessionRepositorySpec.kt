package com.mataku.scrobscrob.data.repository

import app.cash.turbine.test
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.endpoint.AuthMobileSessionEndpoint
import com.mataku.scrobscrob.data.api.endpoint.Endpoint
import com.mataku.scrobscrob.data.api.model.AuthMobileSessionApiResponse
import com.mataku.scrobscrob.data.api.model.MobileSessionBody
import com.mataku.scrobscrob.data.db.ScrobbleAppDataStore
import com.mataku.scrobscrob.data.db.SessionKeyDataStore
import com.mataku.scrobscrob.data.db.UsernameDataStore
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.flowOf

class SessionRepositorySpec : DescribeSpec({
  describe("authorize") {
    it("posts AuthMobileSessionEndpoint, persists session key and username, then emits Unit") {
      val username = "matakucom"
      val password = "secret"
      val sessionKey = "abcdef0123456789"
      val service = mockk<LastFmService>()
      val sessionKeyDataStore = mockk<SessionKeyDataStore>()
      val usernameDataStore = mockk<UsernameDataStore>()
      val scrobbleAppDataStore = mockk<ScrobbleAppDataStore>()

      val slot = slot<Endpoint<*>>()
      val fakeResponse = AuthMobileSessionApiResponse(
        mobileSession = MobileSessionBody(
          name = username,
          key = sessionKey,
        ),
      )
      coEvery { service.rawRequest(capture(slot), any()) } returns fakeResponse
      coEvery { sessionKeyDataStore.setSessionKey(sessionKey) } returns flowOf(Unit)
      coEvery { usernameDataStore.setUsername(username) } returns flowOf(Unit)

      val repository = SessionRepositoryImpl(
        service, sessionKeyDataStore, usernameDataStore, scrobbleAppDataStore,
      )
      repository.authorize(userName = username, password = password).test {
        awaitItem() shouldBe Unit
        awaitComplete()
      }

      coVerify(exactly = 1) { sessionKeyDataStore.setSessionKey(sessionKey) }
      coVerify(exactly = 1) { usernameDataStore.setUsername(username) }

      val captured = slot.captured
      captured.shouldBeInstanceOf<AuthMobileSessionEndpoint>()
      captured.params["username"] shouldBe username
      captured.params["password"] shouldBe password
      captured.params["api_sig"].shouldBeInstanceOf<String>().shouldNotBeBlank()
      captured.params["method"] shouldBe "auth.getMobileSession"
    }
  }

  describe("logout") {
    it("clears session key, username, and allowed apps then emits Unit") {
      val service = mockk<LastFmService>()
      val sessionKeyDataStore = mockk<SessionKeyDataStore>()
      val usernameDataStore = mockk<UsernameDataStore>()
      val scrobbleAppDataStore = mockk<ScrobbleAppDataStore>()
      coEvery { sessionKeyDataStore.remove() } returns Unit
      coEvery { usernameDataStore.remove() } returns Unit
      coEvery { scrobbleAppDataStore.clear() } returns Unit

      val repository = SessionRepositoryImpl(
        service, sessionKeyDataStore, usernameDataStore, scrobbleAppDataStore,
      )
      repository.logout().test {
        awaitItem() shouldBe Unit
        awaitComplete()
      }

      coVerify(exactly = 1) { sessionKeyDataStore.remove() }
      coVerify(exactly = 1) { usernameDataStore.remove() }
      coVerify(exactly = 1) { scrobbleAppDataStore.clear() }
      coVerify(exactly = 0) { service.rawRequest(any(), any()) }
    }
  }
})
