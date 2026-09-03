package com.mataku.scrobscrob.data.repository

import app.cash.turbine.test
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.endpoint.Endpoint
import com.mataku.scrobscrob.data.api.endpoint.LoveTrackEndpoint
import com.mataku.scrobscrob.data.api.endpoint.TrackInfoEndpoint
import com.mataku.scrobscrob.data.api.endpoint.TrackInfoApiResponse
import com.mataku.scrobscrob.data.api.endpoint.UnLoveTrackEndpoint
import com.mataku.scrobscrob.data.api.model.ImageBody
import com.mataku.scrobscrob.data.api.model.TrackAlbumInfoBody
import com.mataku.scrobscrob.data.api.model.TrackArtistBody
import com.mataku.scrobscrob.data.api.model.TrackInfoBody
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

class TrackRepositorySpec : DescribeSpec({
  describe("getInfo") {
    it("builds TrackInfoEndpoint and maps to TrackInfo") {
      val artistName = "aespa"
      val trackName = "Drama"
      val username = "matakucom"
      val service = mockk<LastFmService>()
      val usernameDataStore = mockk<UsernameDataStore>()
      val sessionKeyDataStore = mockk<SessionKeyDataStore>()
      coEvery { usernameDataStore.username() } returns username

      val slot = slot<Endpoint<*>>()
      val fakeResponse = TrackInfoApiResponse(
        trackInfo = TrackInfoBody(
          duration = 214000L,
          album = TrackAlbumInfoBody(
            artist = "aespa",
            title = "Drama - The 4th Mini Album",
            imageList = listOf(
              ImageBody(size = "extralarge", url = "https://example.com/image.jpg"),
            ),
          ),
          listeners = "120000",
          playCount = "1500000",
          url = "https://www.last.fm/music/aespa/_/Drama",
          artist = TrackArtistBody(name = "aespa", url = "https://www.last.fm/music/aespa"),
          name = "Drama",
          userPlayCount = "42",
          userLoved = "1",
        ),
      )
      coEvery { service.rawRequest(capture(slot), any()) } returns fakeResponse

      val repository = TrackRepositoryImpl(service, usernameDataStore, sessionKeyDataStore)
      repository.getInfo(trackName = trackName, artistName = artistName).test {
        awaitItem().let { trackInfo ->
          trackInfo.name shouldBe "Drama"
          trackInfo.artist.name shouldBe "aespa"
          trackInfo.album?.title shouldBe "Drama - The 4th Mini Album"
          trackInfo.duration shouldBe 214000L
          trackInfo.listeners shouldBe "120000"
          trackInfo.playCount shouldBe "1500000"
          trackInfo.userPlayCount shouldBe "42"
          trackInfo.userLoved shouldBe true
        }
        awaitComplete()
      }

      val captured = slot.captured
      captured.shouldBeInstanceOf<TrackInfoEndpoint>()
      captured.params shouldBe mapOf(
        "artist" to artistName,
        "track" to trackName,
        "username" to username,
      )
    }
  }

  describe("loveTrack") {
    context("session key is null") {
      it("propagates IllegalStateException without calling the API") {
        val service = mockk<LastFmService>()
        val usernameDataStore = mockk<UsernameDataStore>()
        val sessionKeyDataStore = mockk<SessionKeyDataStore>()
        coEvery { sessionKeyDataStore.sessionKey() } returns null

        val repository = TrackRepositoryImpl(service, usernameDataStore, sessionKeyDataStore)
        repository.loveTrack(trackName = "Drama", artistName = "aespa").test {
          awaitError().shouldBeInstanceOf<IllegalStateException>()
        }
        coVerify(exactly = 0) { service.rawRequest(any(), any()) }
      }
    }

    context("session key is set") {
      it("posts LoveTrackEndpoint with signed params and emits Unit") {
        val service = mockk<LastFmService>()
        val usernameDataStore = mockk<UsernameDataStore>()
        val sessionKeyDataStore = mockk<SessionKeyDataStore>()
        val sessionKey = "sessionkey"
        coEvery { sessionKeyDataStore.sessionKey() } returns sessionKey

        val slot = slot<Endpoint<*>>()
        coEvery { service.rawRequest(capture(slot), any()) } returns Unit

        val repository = TrackRepositoryImpl(service, usernameDataStore, sessionKeyDataStore)
        repository.loveTrack(trackName = "Drama", artistName = "aespa").test {
          awaitItem() shouldBe Unit
          awaitComplete()
        }

        val captured = slot.captured
        captured.shouldBeInstanceOf<LoveTrackEndpoint>()
        captured.params["artist"] shouldBe "aespa"
        captured.params["track"] shouldBe "Drama"
        captured.params["sk"] shouldBe sessionKey
        captured.params["api_sig"].shouldBeInstanceOf<String>().shouldNotBeBlank()
        captured.params.containsKey("method") shouldBe false
      }
    }
  }

  describe("unloveTrack") {
    context("session key is null") {
      it("propagates IllegalStateException without calling the API") {
        val service = mockk<LastFmService>()
        val usernameDataStore = mockk<UsernameDataStore>()
        val sessionKeyDataStore = mockk<SessionKeyDataStore>()
        coEvery { sessionKeyDataStore.sessionKey() } returns null

        val repository = TrackRepositoryImpl(service, usernameDataStore, sessionKeyDataStore)
        repository.unloveTrack(trackName = "Drama", artistName = "aespa").test {
          awaitError().shouldBeInstanceOf<IllegalStateException>()
        }
        coVerify(exactly = 0) { service.rawRequest(any(), any()) }
      }
    }

    context("session key is set") {
      it("posts UnLoveTrackEndpoint with signed params and emits Unit") {
        val service = mockk<LastFmService>()
        val usernameDataStore = mockk<UsernameDataStore>()
        val sessionKeyDataStore = mockk<SessionKeyDataStore>()
        val sessionKey = "sessionkey"
        coEvery { sessionKeyDataStore.sessionKey() } returns sessionKey

        val slot = slot<Endpoint<*>>()
        coEvery { service.rawRequest(capture(slot), any()) } returns Unit

        val repository = TrackRepositoryImpl(service, usernameDataStore, sessionKeyDataStore)
        repository.unloveTrack(trackName = "Drama", artistName = "aespa").test {
          awaitItem() shouldBe Unit
          awaitComplete()
        }

        val captured = slot.captured
        captured.shouldBeInstanceOf<UnLoveTrackEndpoint>()
        captured.params["artist"] shouldBe "aespa"
        captured.params["track"] shouldBe "Drama"
        captured.params["sk"] shouldBe sessionKey
        captured.params["api_sig"].shouldBeInstanceOf<String>().shouldNotBeBlank()
        captured.params.containsKey("method") shouldBe false
      }
    }
  }
})
