package com.mataku.scrobscrob.data.repository

import app.cash.turbine.test
import com.mataku.scrobscrob.core.entity.TimeRangeFiltering
import com.mataku.scrobscrob.data.api.LastFmHttpClient
import com.mataku.scrobscrob.data.api.LastFmService
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.fullPath
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel

class AlbumRepositorySpec : DescribeSpec({
  describe("fetchTopAlbums") {
    val page = 1
    val username = "sunsetscrob"
    val timeRangeFiltering = TimeRangeFiltering.LAST_30_DAYS
    it("should parse as TopAlbums") {
      val response = """
        {
          "topalbums": {
            "album": [
              {
                "artist": {
                  "url": "https://www.last.fm/music/PassCode",
                  "name": "PassCode",
                  "mbid": "2b875bcd-6f09-4b70-87fc-17d3f17e6097"
                },
                "image": [
                  {
                    "size": "small",
                    "#text": "https://lastfm.freetls.fastly.net/i/u/34s/580662fd9ffce7415f00c9130fc3d816.jpg"
                  },
                  {
                    "size": "medium",
                    "#text": "https://lastfm.freetls.fastly.net/i/u/64s/580662fd9ffce7415f00c9130fc3d816.jpg"
                  },
                  {
                    "size": "large",
                    "#text": "https://lastfm.freetls.fastly.net/i/u/174s/580662fd9ffce7415f00c9130fc3d816.jpg"
                  },
                  {
                    "size": "extralarge",
                    "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/580662fd9ffce7415f00c9130fc3d816.jpg"
                  }
                ],
                "mbid": "0ef5b9ba-3206-4472-a0ed-b891f6db1693",
                "url": "https://www.last.fm/music/PassCode/ZENITH",
                "playcount": "4118",
                "@attr": {
                  "rank": "1"
                },
                "name": "ZENITH"
              }
            ],
            "@attr": {
              "user": "matakucom",
              "totalPages": "1654",
              "page": "1",
              "perPage": "1",
              "total": "1654"
            }
          }
        }
      """.trimIndent()
      val mockEngine = MockEngine { request ->
        request.url.fullPath shouldBe "/2.0/?method=user.getTopAlbums&format=json&limit=20&page=${page}&period=${timeRangeFiltering.rawValue}&user=${username}"
        request.method shouldBe HttpMethod.Get
        respond(
          content = ByteReadChannel(response),
          status = HttpStatusCode.OK,
          headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
      }
      val lastFmService = LastFmService(
        LastFmHttpClient.create(mockEngine)
      )
      val repository = AlbumRepositoryImpl(lastFmService)
      repository.fetchTopAlbums(
        page = page,
        username = username,
        timeRangeFiltering = timeRangeFiltering
      ).test {
        awaitItem().let {
          it.size shouldBe 1
          it[0].let { albumInfo ->
            albumInfo.artist shouldBe "PassCode"
            albumInfo.title shouldBe "ZENITH"
            albumInfo.imageList.isNotEmpty() shouldBe true
            albumInfo.playCount shouldBe "4118"
            albumInfo.url shouldBe "https://www.last.fm/music/PassCode/ZENITH"
          }
        }
        awaitComplete()
      }
    }
  }
})
