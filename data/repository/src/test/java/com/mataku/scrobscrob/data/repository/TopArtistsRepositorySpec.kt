package com.mataku.scrobscrob.data.repository

import app.cash.turbine.test
import com.mataku.scrobscrob.core.entity.TimeRangeFiltering
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.endpoint.Endpoint
import com.mataku.scrobscrob.data.api.endpoint.UserTopArtistsEndpoint
import com.mataku.scrobscrob.data.api.model.ArtistBody
import com.mataku.scrobscrob.data.api.model.ImageBody
import com.mataku.scrobscrob.data.api.model.PagingAttrBody
import com.mataku.scrobscrob.data.api.model.TopArtistsBody
import com.mataku.scrobscrob.data.api.model.UserTopArtistsApiResponse
import com.mataku.scrobscrob.data.db.ArtworkDataStore
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot

class TopArtistsRepositorySpec : DescribeSpec({
  describe("fetchTopArtists") {
    it("builds UserTopArtistsEndpoint with limit/page/period/user and maps to TopArtists") {
      val page = 1
      val username = "sunsetscrob"
      val timeRange = TimeRangeFiltering.LAST_30_DAYS
      val service = mockk<LastFmService>()
      val artworkDataStore = mockk<ArtworkDataStore>(relaxed = true)
      val slot = slot<Endpoint<*>>()
      val fakeResponse = UserTopArtistsApiResponse(
        topArtists = TopArtistsBody(
          artists = listOf(
            ArtistBody(
              name = "PassCode",
              url = "https://www.last.fm/music/PassCode",
              playcount = "5793",
              imageList = listOf(
                ImageBody(
                  size = "large",
                  url = "https://lastfm-img2.akamaized.net/i/u/174s/3a6201efff969f30b97d94a3586ec2ba.png",
                ),
              ),
            ),
          ),
          pagingAttrBody = PagingAttrBody(
            page = "1",
            perPage = "20",
            totalPages = "24",
            total = "118",
          ),
        ),
      )
      coEvery { service.rawRequest(capture(slot), any()) } returns fakeResponse

      val repository = TopArtistsRepositoryImpl(service, artworkDataStore)
      repository.fetchTopArtists(
        page = page,
        username = username,
        timeRangeFiltering = timeRange,
      ).test {
        val item = awaitItem()
        item.artists.size shouldBe 1
        item.artists[0].let { artist ->
          artist.name shouldBe "PassCode"
          artist.playCount shouldBe "5793"
          artist.url shouldBe "https://www.last.fm/music/PassCode"
        }
        awaitComplete()
      }

      val captured = slot.captured
      captured.shouldBeInstanceOf<UserTopArtistsEndpoint>()
      captured.params shouldBe mapOf(
        "limit" to 20,
        "page" to page,
        "period" to timeRange.rawValue,
        "user" to username,
      )
    }
  }
})
