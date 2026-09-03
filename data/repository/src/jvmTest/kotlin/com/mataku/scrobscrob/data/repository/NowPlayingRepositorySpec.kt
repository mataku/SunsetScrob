package com.mataku.scrobscrob.data.repository

import app.cash.turbine.test
import com.mataku.scrobscrob.core.entity.TrackAlbumInfo
import com.mataku.scrobscrob.core.entity.TrackArtist
import com.mataku.scrobscrob.core.entity.TrackInfo
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.endpoint.Endpoint
import com.mataku.scrobscrob.data.api.endpoint.UpdateNowPlayingEndpoint
import com.mataku.scrobscrob.data.api.model.NowPlayingAlbumBody
import com.mataku.scrobscrob.data.api.model.NowPlayingApiResponse
import com.mataku.scrobscrob.data.api.model.NowPlayingArtistBody
import com.mataku.scrobscrob.data.api.model.NowPlayingBody
import com.mataku.scrobscrob.data.api.model.NowPlayingTrackBody
import com.mataku.scrobscrob.data.db.SessionKeyDataStore
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.collections.immutable.persistentListOf

private fun trackInfo(
  name: String = "Drama",
  artistName: String = "aespa",
  albumTitle: String = "Drama - The 4th Mini Album",
): TrackInfo = TrackInfo(
  duration = 214000L,
  artist = TrackArtist(name = artistName, url = "https://www.last.fm/music/$artistName"),
  album = TrackAlbumInfo(
    artist = artistName,
    title = albumTitle,
    imageList = persistentListOf(),
  ),
  listeners = "120000",
  playCount = "1500000",
  url = "https://www.last.fm/music/$artistName/_/$name",
  name = name,
  userPlayCount = "0",
  userLoved = false,
)

class NowPlayingRepositorySpec : DescribeSpec({
  describe("update") {
    context("session key is null") {
      it("caches the now-playing track but skips the API call") {
        val service = mockk<LastFmService>()
        val sessionKeyDataStore = mockk<SessionKeyDataStore>()
        coEvery { sessionKeyDataStore.sessionKey() } returns null

        val repository = NowPlayingRepositoryImpl(service, sessionKeyDataStore)
        val track = trackInfo()

        repository.update(track).test {
          awaitItem() shouldBe Unit
          awaitComplete()
        }
        coVerify(exactly = 0) { service.rawRequest(any(), any()) }

        repository.current().test {
          awaitItem()?.trackName shouldBe "Drama"
          awaitComplete()
        }
      }
    }

    context("session key is set") {
      it("posts UpdateNowPlayingEndpoint with signed params and caches the track") {
        val service = mockk<LastFmService>()
        val sessionKeyDataStore = mockk<SessionKeyDataStore>()
        val sessionKey = "sessionkey"
        coEvery { sessionKeyDataStore.sessionKey() } returns sessionKey

        val slot = slot<Endpoint<*>>()
        val fakeResponse = NowPlayingApiResponse(
          nowPlaying = NowPlayingBody(
            artist = NowPlayingArtistBody(corrected = "0", text = "aespa"),
            album = NowPlayingAlbumBody(corrected = "0", text = "Drama - The 4th Mini Album"),
            track = NowPlayingTrackBody(corrected = "0", text = "Drama"),
          ),
        )
        coEvery { service.rawRequest(capture(slot), any()) } returns fakeResponse

        val repository = NowPlayingRepositoryImpl(service, sessionKeyDataStore)
        val track = trackInfo()

        repository.update(track).test {
          awaitItem() shouldBe Unit
          awaitComplete()
        }

        val captured = slot.captured
        captured.shouldBeInstanceOf<UpdateNowPlayingEndpoint>()
        captured.params["artist"] shouldBe "aespa"
        captured.params["track"] shouldBe "Drama"
        captured.params["album"] shouldBe "Drama - The 4th Mini Album"
        captured.params["sk"] shouldBe sessionKey
        captured.params["api_sig"].shouldBeInstanceOf<String>().shouldNotBeBlank()
        captured.params.containsKey("method") shouldBe false

        repository.current().test {
          awaitItem()?.let { entity ->
            entity.trackName shouldBe "Drama"
            entity.artistName shouldBe "aespa"
            entity.albumName shouldBe "Drama - The 4th Mini Album"
          }
          awaitComplete()
        }
      }
    }
  }

  describe("current") {
    it("emits null when nothing has been cached yet") {
      val service = mockk<LastFmService>()
      val sessionKeyDataStore = mockk<SessionKeyDataStore>()
      val repository = NowPlayingRepositoryImpl(service, sessionKeyDataStore)

      repository.current().test {
        awaitItem() shouldBe null
        awaitComplete()
      }
    }
  }
})
