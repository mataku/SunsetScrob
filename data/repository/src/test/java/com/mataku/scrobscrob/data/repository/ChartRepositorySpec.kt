package com.mataku.scrobscrob.data.repository

import app.cash.turbine.test
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.endpoint.ChartTopArtistsEndpoint
import com.mataku.scrobscrob.data.api.endpoint.ChartTopTracksEndpoint
import com.mataku.scrobscrob.data.api.endpoint.Endpoint
import com.mataku.scrobscrob.data.api.model.ChartArtist
import com.mataku.scrobscrob.data.api.model.ChartTopArtistsBody
import com.mataku.scrobscrob.data.api.model.ChartTopArtistsResponse
import com.mataku.scrobscrob.data.api.model.ChartTopTracksBody
import com.mataku.scrobscrob.data.api.model.ChartTopTracksResponse
import com.mataku.scrobscrob.data.api.model.ChartTrack
import com.mataku.scrobscrob.data.api.model.ChartTrackArtist
import com.mataku.scrobscrob.data.api.model.ImageBody
import com.mataku.scrobscrob.data.api.model.PagingAttrBody
import com.mataku.scrobscrob.data.db.ArtworkDataStore
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot

class ChartRepositorySpec : DescribeSpec({
  describe("topArtists") {
    val page = 1
    val fakeResponse = ChartTopArtistsResponse(
      chartTopArtistsBody = ChartTopArtistsBody(
        topArtists = listOf(
          ChartArtist(
            name = "The Weeknd",
            playCount = "578258095",
            listeners = "3680672",
            url = "https://www.last.fm/music/The+Weeknd",
            imageList = listOf(
              ImageBody(
                size = "extralarge",
                url = "https://lastfm.freetls.fastly.net/i/u/300x300/2a96cbd8b46e442fc41c2b86b821562f.png",
              ),
            ),
          ),
        ),
        pagingAttrBody = PagingAttrBody(
          page = "1",
          perPage = "1",
          totalPages = "5749773",
          total = "5749773",
        ),
      ),
    )

    context("when artwork lookup returns null") {
      it("emits artists pass-through and asserts captured endpoint") {
        val service = mockk<LastFmService>()
        val artworkDataStore = mockk<ArtworkDataStore>()
        coEvery { artworkDataStore.artwork(artist = any()) } returns null
        val slot = slot<Endpoint<*>>()
        coEvery { service.rawRequest(capture(slot), any()) } returns fakeResponse

        val repository = ChartRepositoryImpl(service, artworkDataStore)
        repository.topArtists(page).test {
          val item = awaitItem()
          item.topArtists.size shouldBe 1
          item.topArtists[0].let { artist ->
            artist.name shouldBe "The Weeknd"
            artist.playCount shouldBe "578258095"
            artist.listeners shouldBe "3680672"
            artist.url shouldBe "https://www.last.fm/music/The+Weeknd"
            artist.imageUrl shouldBe null
          }
          awaitComplete()
        }

        val captured = slot.captured
        captured.shouldBeInstanceOf<ChartTopArtistsEndpoint>()
        captured.params shouldBe mapOf("limit" to "10", "page" to page.toString())
      }
    }

    context("when artwork lookup returns a cached URL") {
      it("overrides the artist's imageUrl") {
        val service = mockk<LastFmService>()
        val artworkDataStore = mockk<ArtworkDataStore>()
        coEvery { artworkDataStore.artwork(artist = "The Weeknd") } returns "https://cached.example/weeknd.png"
        coEvery { service.rawRequest(any(), any()) } returns fakeResponse

        val repository = ChartRepositoryImpl(service, artworkDataStore)
        repository.topArtists(page).test {
          awaitItem().topArtists[0].imageUrl shouldBe "https://cached.example/weeknd.png"
          awaitComplete()
        }
      }
    }
  }

  describe("topTracks") {
    val page = 1

    context("when track has valid artwork in imageList") {
      it("emits tracks without invoking artworkDataStore") {
        val fakeResponse = ChartTopTracksResponse(
          chartTopTracksBody = ChartTopTracksBody(
            topTracks = listOf(
              ChartTrack(
                name = "My Love Mine All Mine",
                playCount = "16319592",
                listeners = "843008",
                url = "https://www.last.fm/music/Mitski/_/My+Love+Mine+All+Mine",
                artist = ChartTrackArtist(
                  name = "Mitski",
                  url = "https://www.last.fm/music/Mitski",
                ),
                imageList = listOf(
                  ImageBody(
                    size = "extralarge",
                    url = "https://lastfm.freetls.fastly.net/i/u/300x300/real-track-image.png",
                  ),
                ),
                mbid = "",
              ),
            ),
            pagingAttrBody = PagingAttrBody(
              page = "1",
              perPage = "1",
              totalPages = "37697653",
              total = "37697653",
            ),
          ),
        )
        val service = mockk<LastFmService>()
        val artworkDataStore = mockk<ArtworkDataStore>()
        val slot = slot<Endpoint<*>>()
        coEvery { service.rawRequest(capture(slot), any()) } returns fakeResponse

        val repository = ChartRepositoryImpl(service, artworkDataStore)
        repository.topTracks(page).test {
          val item = awaitItem()
          item.topTracks.size shouldBe 1
          item.topTracks[0].name shouldBe "My Love Mine All Mine"
          item.topTracks[0].artist.name shouldBe "Mitski"
          awaitComplete()
        }

        val captured = slot.captured
        captured.shouldBeInstanceOf<ChartTopTracksEndpoint>()
        captured.params shouldBe mapOf("limit" to "10", "page" to page.toString())

        coVerify(exactly = 0) { artworkDataStore.artwork(any()) }
      }
    }

    context("when track has invalid artwork -> cached override") {
      it("overrides imageUrl with the cached value") {
        val fakeResponse = ChartTopTracksResponse(
          chartTopTracksBody = ChartTopTracksBody(
            topTracks = listOf(
              ChartTrack(
                name = "My Love Mine All Mine",
                playCount = "16319592",
                listeners = "843008",
                url = "https://www.last.fm/music/Mitski/_/My+Love+Mine+All+Mine",
                artist = ChartTrackArtist(
                  name = "Mitski",
                  url = "https://www.last.fm/music/Mitski",
                ),
                imageList = emptyList(),
                mbid = "",
              ),
            ),
            pagingAttrBody = PagingAttrBody(
              page = "1",
              perPage = "1",
              totalPages = "37697653",
              total = "37697653",
            ),
          ),
        )
        val service = mockk<LastFmService>()
        val artworkDataStore = mockk<ArtworkDataStore>()
        coEvery { artworkDataStore.artwork(artist = "Mitski") } returns "https://cached.example/mitski.png"
        coEvery { service.rawRequest(any(), any()) } returns fakeResponse

        val repository = ChartRepositoryImpl(service, artworkDataStore)
        repository.topTracks(page).test {
          awaitItem().topTracks[0].imageUrl shouldBe "https://cached.example/mitski.png"
          awaitComplete()
        }
      }
    }
  }
})
