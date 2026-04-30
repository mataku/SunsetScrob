package com.mataku.scrobscrob.data.repository

import app.cash.turbine.test
import com.mataku.scrobscrob.core.entity.NowPlayingTrackEntity
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.endpoint.Endpoint
import com.mataku.scrobscrob.data.api.endpoint.ScrobbleEndpoint
import com.mataku.scrobscrob.data.api.endpoint.UserRecentTracksEndpoint
import com.mataku.scrobscrob.data.api.model.IgnoredMessage
import com.mataku.scrobscrob.data.api.model.ImageBody
import com.mataku.scrobscrob.data.api.model.RecentTrack
import com.mataku.scrobscrob.data.api.model.RecentTrackAlbumBody
import com.mataku.scrobscrob.data.api.model.RecentTrackArtistBody
import com.mataku.scrobscrob.data.api.model.RecentTrackAttrBody
import com.mataku.scrobscrob.data.api.model.RecentTrackDateBody
import com.mataku.scrobscrob.data.api.model.RecentTracksApiResponse
import com.mataku.scrobscrob.data.api.model.RecentTracksBody
import com.mataku.scrobscrob.data.api.model.ScrobbleApiResponse
import com.mataku.scrobscrob.data.api.model.ScrobbleAttrBody
import com.mataku.scrobscrob.data.api.model.ScrobbleBody
import com.mataku.scrobscrob.data.api.model.ScrobbleResultBody
import com.mataku.scrobscrob.data.api.model.ScrobbleValueResult
import com.mataku.scrobscrob.data.db.ArtworkDataStore
import com.mataku.scrobscrob.data.db.SessionKeyDataStore
import com.mataku.scrobscrob.data.db.UsernameDataStore
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.unmockkAll

class ScrobbleRepositorySpec : DescribeSpec({
  val usernameDataStore = mockk<UsernameDataStore>()
  val sessionKeyDataStore = mockk<SessionKeyDataStore>()
  val artworkDataStore = mockk<ArtworkDataStore>(relaxed = true)
  val username = "sunsetscrob"
  val sessionKey = "sessionkey"

  beforeSpec {
    coEvery { usernameDataStore.username() } returns username
    coEvery { sessionKeyDataStore.sessionKey() } returns sessionKey
  }
  afterSpec { unmockkAll() }

  describe("recentTracks") {
    it("builds UserRecentTracksEndpoint and maps to RecentTracks") {
      val service = mockk<LastFmService>()
      val slot = slot<Endpoint<*>>()
      val fakeResponse = RecentTracksApiResponse(
        recentTracks = RecentTracksBody(
          tracks = listOf(
            RecentTrack(
              artist = RecentTrackArtistBody(name = "aespa"),
              images = listOf(
                ImageBody(
                  size = "large",
                  url = "https://lastfm.freetls.fastly.net/i/u/174s/07bc2400d02a125e7b1ef0858ca57d71.jpg",
                ),
              ),
              album = RecentTrackAlbumBody(name = "Drama - The 4th Mini Album"),
              name = "Drama",
              url = "https://www.last.fm/music/aespa/_/Drama",
              date = RecentTrackDateBody(date = "30 Dec 2023, 15:50"),
            ),
          ),
          attr = RecentTrackAttrBody(
            user = "matakucom",
            totalPages = "99951",
          ),
        ),
      )
      coEvery { service.rawRequest(capture(slot), any()) } returns fakeResponse

      val repository = ScrobbleRepositoryImpl(
        service, usernameDataStore, sessionKeyDataStore, artworkDataStore,
      )
      val page = 1
      repository.recentTracks(page = page).test {
        awaitItem().let { result ->
          result.tracks.size shouldBe 1
          result.tracks[0].let { track ->
            track.artistName shouldBe "aespa"
            track.albumName shouldBe "Drama - The 4th Mini Album"
            track.images.isNotEmpty() shouldBe true
            track.name shouldBe "Drama"
            track.url shouldBe "https://www.last.fm/music/aespa/_/Drama"
            track.date shouldBe "30 Dec 2023, 15:50"
          }
          result.pagingAttr.totalPages shouldBe "99951"
        }
        awaitComplete()
      }

      val captured = slot.captured
      captured.shouldBeInstanceOf<UserRecentTracksEndpoint>()
      captured.params shouldBe mapOf("user" to username, "limit" to 50, "page" to page)
    }
  }

  describe("scrobble") {
    context("session key is null") {
      it("emits accepted=false without calling the API") {
        coEvery { sessionKeyDataStore.sessionKey() } returns null
        val service = mockk<LastFmService>()
        val repository = ScrobbleRepositoryImpl(
          service, usernameDataStore, sessionKeyDataStore, artworkDataStore,
        )

        repository.scrobble(mockk()).test {
          awaitItem().accepted shouldBe false
          awaitComplete()
        }
        coVerify(exactly = 0) { service.rawRequest(any(), any()) }
      }
    }

    context("session key is empty") {
      it("emits accepted=false without calling the API") {
        coEvery { sessionKeyDataStore.sessionKey() } returns ""
        val service = mockk<LastFmService>()
        val repository = ScrobbleRepositoryImpl(
          service, usernameDataStore, sessionKeyDataStore, artworkDataStore,
        )

        repository.scrobble(mockk()).test {
          awaitItem().accepted shouldBe false
          awaitComplete()
        }
        coVerify(exactly = 0) { service.rawRequest(any(), any()) }
      }
    }

    context("track is not over scrobble point") {
      it("emits accepted=false without calling the API") {
        coEvery { sessionKeyDataStore.sessionKey() } returns sessionKey
        val service = mockk<LastFmService>()
        val track = mockk<NowPlayingTrackEntity>()
        every { track.overScrobblePoint() } returns false

        val repository = ScrobbleRepositoryImpl(
          service, usernameDataStore, sessionKeyDataStore, artworkDataStore,
        )

        repository.scrobble(track).test {
          awaitItem().accepted shouldBe false
          awaitComplete()
        }
        coVerify(exactly = 0) { service.rawRequest(any(), any()) }
      }
    }

    context("track is over scrobble point") {
      it("posts ScrobbleEndpoint with track metadata and returns accepted=true") {
        coEvery { sessionKeyDataStore.sessionKey() } returns sessionKey
        val service = mockk<LastFmService>()
        val slot = slot<Endpoint<*>>()
        val fakeResponse = ScrobbleApiResponse(
          scrobbleResult = ScrobbleResultBody(
            attr = ScrobbleAttrBody(
              accepted = 1,
              ignored = 0,
            ),
            scrobble = ScrobbleBody(
              artist = ScrobbleValueResult(
                corrected = "0",
                name = "Power Music Workout",
              ),
              ignoredMessage = IgnoredMessage(
                code = "0",
                message = "",
              ),
              timestamp = "1502971625",
              album = ScrobbleValueResult(
                corrected = "0",
                name = "Smash Hits",
              ),
              track = ScrobbleValueResult(
                corrected = "0",
                name = "See You Again",
              ),
            ),
          ),
        )
        coEvery { service.rawRequest(capture(slot), any()) } returns fakeResponse

        val track = mockk<NowPlayingTrackEntity>(relaxed = true)
        every { track.overScrobblePoint() } returns true
        every { track.artistName } returns "Power Music Workout"
        every { track.albumName } returns "Smash Hits"
        every { track.trackName } returns "See You Again"
        every { track.timeStamp } returns 300000L

        val repository = ScrobbleRepositoryImpl(
          service, usernameDataStore, sessionKeyDataStore, artworkDataStore,
        )

        repository.scrobble(track).test {
          awaitItem().accepted shouldBe true
          awaitComplete()
        }

        val captured = slot.captured
        captured.shouldBeInstanceOf<ScrobbleEndpoint>()
        captured.params["album[0]"] shouldBe "Smash Hits"
        captured.params["artist[0]"] shouldBe "Power Music Workout"
        captured.params["track[0]"] shouldBe "See You Again"
        captured.params["sk"] shouldBe sessionKey
        captured.params["timestamp[0]"] shouldBe "300000"
        captured.params["api_sig"].shouldBeInstanceOf<String>().shouldNotBeBlank()
        captured.params.containsKey("method") shouldBe false
      }
    }
  }
})
