package com.mataku.scrobscrob.data.repository

import app.cash.turbine.test
import com.mataku.scrobscrob.data.api.BuildConfig
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.endpoint.Endpoint
import com.mataku.scrobscrob.data.api.endpoint.UserLovedTracksEndpoint
import com.mataku.scrobscrob.data.api.model.ImageBody
import com.mataku.scrobscrob.data.api.model.LovedTrackBody
import com.mataku.scrobscrob.data.api.model.LovedTracksBody
import com.mataku.scrobscrob.data.api.model.LovedTracksResponse
import com.mataku.scrobscrob.data.api.model.PagingAttrBody
import com.mataku.scrobscrob.data.api.model.RecentTrackDateBody
import com.mataku.scrobscrob.data.api.model.TrackArtistBody
import com.mataku.scrobscrob.data.db.ArtworkDataStore
import com.mataku.scrobscrob.data.db.UsernameDataStore
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot

class UserRepositorySpec : DescribeSpec({
  describe("getInfo") {
    context("debug build") {
      it("emits the hard-coded debug user without calling the API") {
        // The production code short-circuits to a hard-coded UserInfo when
        // BuildConfig.DEBUG is true. Unit tests run in the debug variant, so
        // this branch is what the test exercises.
        if (!BuildConfig.DEBUG) return@it

        val service = mockk<LastFmService>()
        val usernameDataStore = mockk<UsernameDataStore>()
        val artworkDataStore = mockk<ArtworkDataStore>()
        val repository = UserRepositoryImpl(service, usernameDataStore, artworkDataStore)

        repository.getInfo("ignored").test {
          awaitItem().let { user ->
            user.name shouldBe "matakucom"
            user.playCount shouldBe "102654"
            user.artistCount shouldBe "728"
            user.trackCount shouldBe "2296"
            user.albumCount shouldBe "1753"
          }
          awaitComplete()
        }

        coVerify(exactly = 0) { service.rawRequest(any(), any()) }
      }
    }
  }

  describe("getLovedTracks") {
    context("username is null") {
      it("emits an empty list without calling the API") {
        val service = mockk<LastFmService>()
        val usernameDataStore = mockk<UsernameDataStore>()
        val artworkDataStore = mockk<ArtworkDataStore>()
        coEvery { usernameDataStore.username() } returns null

        val repository = UserRepositoryImpl(service, usernameDataStore, artworkDataStore)
        repository.getLovedTracks(page = 1).test {
          awaitItem().shouldBeEmpty()
          awaitComplete()
        }
        coVerify(exactly = 0) { service.rawRequest(any(), any()) }
      }
    }

    context("username is empty") {
      it("emits an empty list without calling the API") {
        val service = mockk<LastFmService>()
        val usernameDataStore = mockk<UsernameDataStore>()
        val artworkDataStore = mockk<ArtworkDataStore>()
        coEvery { usernameDataStore.username() } returns ""

        val repository = UserRepositoryImpl(service, usernameDataStore, artworkDataStore)
        repository.getLovedTracks(page = 1).test {
          awaitItem().shouldBeEmpty()
          awaitComplete()
        }
        coVerify(exactly = 0) { service.rawRequest(any(), any()) }
      }
    }

    context("username is set") {
      it("builds UserLovedTracksEndpoint and maps to LovedTrack list") {
        val username = "matakucom"
        val page = 1
        val service = mockk<LastFmService>()
        val usernameDataStore = mockk<UsernameDataStore>()
        val artworkDataStore = mockk<ArtworkDataStore>()
        coEvery { usernameDataStore.username() } returns username
        coEvery { artworkDataStore.artwork(any()) } returns null

        val slot = slot<Endpoint<*>>()
        val fakeResponse = LovedTracksResponse(
          lovedTracks = LovedTracksBody(
            tracks = listOf(
              LovedTrackBody(
                artist = TrackArtistBody(
                  name = "aespa",
                  url = "https://www.last.fm/music/aespa",
                ),
                images = listOf(
                  ImageBody(
                    size = "extralarge",
                    url = "https://lastfm.freetls.fastly.net/i/u/300x300/real-image.jpg",
                  ),
                ),
                name = "Drama",
                url = "https://www.last.fm/music/aespa/_/Drama",
                date = RecentTrackDateBody(date = "30 Dec 2023, 15:50"),
              ),
            ),
            attr = PagingAttrBody(
              page = "1",
              perPage = "20",
              totalPages = "12",
              total = "234",
            ),
          ),
        )
        coEvery { service.rawRequest(capture(slot), any()) } returns fakeResponse

        val repository = UserRepositoryImpl(service, usernameDataStore, artworkDataStore)
        repository.getLovedTracks(page = page).test {
          awaitItem().let { tracks ->
            tracks.size shouldBe 1
            tracks[0].artist shouldBe "aespa"
            tracks[0].name shouldBe "Drama"
            tracks[0].url shouldBe "https://www.last.fm/music/aespa/_/Drama"
            tracks[0].date shouldBe "30 Dec 2023, 15:50"
            tracks[0].images.isNotEmpty() shouldBe true
          }
          awaitComplete()
        }

        val captured = slot.captured
        captured.shouldBeInstanceOf<UserLovedTracksEndpoint>()
        captured.params shouldBe mapOf(
          "limit" to "20",
          "page" to page.toString(),
          "user" to username,
        )
      }
    }

    context("artwork is invalid and a cached URL is found") {
      it("overrides the loved track's imageUrl with the cached value") {
        val username = "matakucom"
        val service = mockk<LastFmService>()
        val usernameDataStore = mockk<UsernameDataStore>()
        val artworkDataStore = mockk<ArtworkDataStore>()
        coEvery { usernameDataStore.username() } returns username
        coEvery { artworkDataStore.artwork(artist = "aespa") } returns "https://cached.example/aespa.png"

        val fakeResponse = LovedTracksResponse(
          lovedTracks = LovedTracksBody(
            tracks = listOf(
              LovedTrackBody(
                artist = TrackArtistBody(
                  name = "aespa",
                  url = "https://www.last.fm/music/aespa",
                ),
                images = emptyList(),
                name = "Drama",
                url = "https://www.last.fm/music/aespa/_/Drama",
              ),
            ),
            attr = PagingAttrBody(
              page = "1",
              perPage = "20",
              totalPages = "1",
              total = "1",
            ),
          ),
        )
        coEvery { service.rawRequest(any(), any()) } returns fakeResponse

        val repository = UserRepositoryImpl(service, usernameDataStore, artworkDataStore)
        repository.getLovedTracks(page = 1).test {
          awaitItem()[0].imageUrl shouldBe "https://cached.example/aespa.png"
          awaitComplete()
        }
      }
    }
  }
})
