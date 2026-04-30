package com.mataku.scrobscrob.data.repository

import app.cash.turbine.test
import com.mataku.scrobscrob.core.entity.Stats
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.endpoint.ArtistInfoEndpoint
import com.mataku.scrobscrob.data.api.endpoint.ArtistTopAlbumsEndpoint
import com.mataku.scrobscrob.data.api.endpoint.Endpoint
import com.mataku.scrobscrob.data.api.model.AlbumArtistBody
import com.mataku.scrobscrob.data.api.model.AlbumBody
import com.mataku.scrobscrob.data.api.model.AlbumsBody
import com.mataku.scrobscrob.data.api.model.ArtistInfoApiResponse
import com.mataku.scrobscrob.data.api.model.ArtistInfoBody
import com.mataku.scrobscrob.data.api.model.ImageBody
import com.mataku.scrobscrob.data.api.model.MultipleTag
import com.mataku.scrobscrob.data.api.model.StatsBody
import com.mataku.scrobscrob.data.api.model.TagBody
import com.mataku.scrobscrob.data.api.model.TopAlbumsApiResponse
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot

class ArtistRepositorySpec : DescribeSpec({
  describe("artistInfo") {
    it("builds ArtistInfoEndpoint with the artist param and maps to ArtistInfo") {
      val name = "Nayeon"
      val service = mockk<LastFmService>()
      val slot = slot<Endpoint<*>>()
      val fakeResponse = ArtistInfoApiResponse(
        artistInfo = ArtistInfoBody(
          name = "Nayeon",
          url = "https://www.last.fm/music/Nayeon",
          imageList = listOf(
            ImageBody(
              size = "large",
              url = "https://lastfm.freetls.fastly.net/i/u/174s/2a96cbd8b46e442fc41c2b86b821562f.png",
            ),
          ),
          tags = MultipleTag(
            tagList = listOf(
              TagBody(
                name = "k-pop",
                url = "https://www.last.fm/tag/k-pop",
              ),
            ),
          ),
          stats = StatsBody(
            listeners = "384242",
            playCount = "15045020",
          ),
        ),
      )
      coEvery { service.rawRequest(capture(slot), any()) } returns fakeResponse

      val repository = ArtistRepositoryImpl(service)
      repository.artistInfo(name = name).test {
        awaitItem().let {
          it.name shouldBe "Nayeon"
          it.images.isNotEmpty() shouldBe true
          it.url shouldBe "https://www.last.fm/music/Nayeon"
          it.tags.isNotEmpty() shouldBe true
          it.stats shouldBe Stats(listeners = "384242", playCount = "15045020")
        }
        awaitComplete()
      }

      val captured = slot.captured
      captured.shouldBeInstanceOf<ArtistInfoEndpoint>()
      captured.params shouldBe mapOf("artist" to name)
    }
  }

  describe("topAlbums") {
    it("builds ArtistTopAlbumsEndpoint with artist/page/limit and maps to a non-empty list") {
      val name = "aespa"
      val page = 1
      val limit = 1
      val service = mockk<LastFmService>()
      val slot = slot<Endpoint<*>>()
      val fakeResponse = TopAlbumsApiResponse(
        topAlbums = AlbumsBody(
          albums = listOf(
            AlbumBody(
              name = "SAVAGE - The 1st Mini Album",
              url = "https://www.last.fm/music/aespa/SAVAGE+-+The+1st+Mini+Album",
              artist = AlbumArtistBody(
                name = "aespa",
              ),
              playcount = "20996309",
            ),
          ),
        ),
      )
      coEvery { service.rawRequest(capture(slot), any()) } returns fakeResponse

      val repository = ArtistRepositoryImpl(service)
      repository.topAlbums(name = name, page = page, limit = limit).test {
        val albums = awaitItem()
        albums.size shouldBe 1
        albums[0].let { album ->
          album.title shouldBe "SAVAGE - The 1st Mini Album"
          album.artist shouldBe "aespa"
          album.playCount shouldBe "20996309"
        }
        awaitComplete()
      }

      val captured = slot.captured
      captured.shouldBeInstanceOf<ArtistTopAlbumsEndpoint>()
      captured.params shouldBe mapOf(
        "artist" to name,
        "page" to page.toString(),
        "limit" to limit.toString(),
      )
    }
  }
})
