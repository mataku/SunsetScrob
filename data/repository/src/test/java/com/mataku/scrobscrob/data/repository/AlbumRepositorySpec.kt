package com.mataku.scrobscrob.data.repository

import app.cash.turbine.test
import com.mataku.scrobscrob.core.entity.TimeRangeFiltering
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.endpoint.AlbumInfoEndpoint
import com.mataku.scrobscrob.data.api.endpoint.Endpoint
import com.mataku.scrobscrob.data.api.endpoint.UserTopAlbumsEndpoint
import com.mataku.scrobscrob.data.api.model.AlbumArtistBody
import com.mataku.scrobscrob.data.api.model.AlbumBody
import com.mataku.scrobscrob.data.api.model.AlbumInfoBody
import com.mataku.scrobscrob.data.api.model.AlbumInfoResponse
import com.mataku.scrobscrob.data.api.model.AlbumInfoTrackBody
import com.mataku.scrobscrob.data.api.model.AlbumsBody
import com.mataku.scrobscrob.data.api.model.ImageBody
import com.mataku.scrobscrob.data.api.model.MultipleTag
import com.mataku.scrobscrob.data.api.model.PagingAttrBody
import com.mataku.scrobscrob.data.api.model.TagBody
import com.mataku.scrobscrob.data.api.model.TopAlbumsApiResponse
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot

class AlbumRepositorySpec : DescribeSpec({
  describe("fetchTopAlbums") {
    it("builds UserTopAlbumsEndpoint with the right params and maps to TopAlbums") {
      val page = 1
      val username = "sunsetscrob"
      val timeRangeFiltering = TimeRangeFiltering.LAST_30_DAYS
      val service = mockk<LastFmService>()
      val slot = slot<Endpoint<*>>()
      val fakeResponse = TopAlbumsApiResponse(
        topAlbums = AlbumsBody(
          albums = listOf(
            AlbumBody(
              name = "ZENITH",
              url = "https://www.last.fm/music/PassCode/ZENITH",
              artist = AlbumArtistBody(name = "PassCode"),
              imageList = listOf(ImageBody(size = "large", url = "https://example.com/image.jpg")),
              playcount = "4118",
            ),
          ),
          pagingAttrBody = PagingAttrBody(
            page = "1",
            perPage = "1",
            totalPages = "1654",
            total = "1654",
          ),
        ),
      )
      coEvery { service.rawRequest(capture(slot), any()) } returns fakeResponse

      val repository = AlbumRepositoryImpl(service)

      repository.fetchTopAlbums(
        page = page,
        username = username,
        timeRangeFiltering = timeRangeFiltering,
      ).test {
        awaitItem().let { result ->
          result.albums.size shouldBe 1
          result.pagingAttr.totalPages shouldBe "1654"
        }
        awaitComplete()
      }

      val captured = slot.captured
      captured.shouldBeInstanceOf<UserTopAlbumsEndpoint>()
      captured.params shouldBe mapOf(
        "limit" to 20,
        "page" to page,
        "period" to timeRangeFiltering.rawValue,
        "user" to username,
      )
    }
  }

  describe("albumInfo") {
    it("builds AlbumInfoEndpoint with the right params and maps to AlbumInfo") {
      val albumName = "Drama"
      val artistName = "aespa"
      val service = mockk<LastFmService>()
      val slot = slot<Endpoint<*>>()
      val fakeResponse = AlbumInfoResponse(
        albumInfoBody = AlbumInfoBody(
          albumName = "Drama",
          artistName = "aespa",
          url = "https://www.last.fm/music/aespa/Drama",
          images = listOf(ImageBody(size = "large", url = "https://example.com/image.jpg")),
          listeners = "1594",
          playCount = "170051",
          tracks = AlbumInfoTrackBody(
            tracks = listOf(
              AlbumInfoTrackBody.AlbumInfoTrackEntity(
                duration = "214",
                url = "https://www.last.fm/music/aespa/Drama/Drama",
                name = "Drama",
              ),
            ),
          ),
          tagsBody = MultipleTag(
            tagList = listOf(TagBody(name = "k-pop", url = "https://www.last.fm/tag/k-pop")),
          ),
        ),
      )
      coEvery { service.rawRequest(capture(slot), any()) } returns fakeResponse

      val repository = AlbumRepositoryImpl(service)

      repository.albumInfo(
        albumName = albumName,
        artistName = artistName,
      ).test {
        awaitItem().let {
          it.albumName shouldBe "Drama"
          it.artistName shouldBe "aespa"
          it.images.isNotEmpty() shouldBe true
          it.tracks.isNotEmpty() shouldBe true
          it.listeners shouldBe "1594"
          it.playCount shouldBe "170051"
          it.tags.isNotEmpty() shouldBe true
        }
        awaitComplete()
      }

      val captured = slot.captured
      captured.shouldBeInstanceOf<AlbumInfoEndpoint>()
      captured.params shouldBe mapOf("album" to albumName, "artist" to artistName)
    }
  }
})
