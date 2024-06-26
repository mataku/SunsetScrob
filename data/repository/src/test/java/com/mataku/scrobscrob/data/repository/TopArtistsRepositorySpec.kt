package com.mataku.scrobscrob.data.repository

import app.cash.turbine.test
import com.mataku.scrobscrob.core.entity.TimeRangeFiltering
import com.mataku.scrobscrob.data.api.LastFmHttpClient
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.db.ArtworkDataStore
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
import io.mockk.mockk

class TopArtistsRepositorySpec : DescribeSpec({
  describe("fetchTopArtists") {
    val limit = 20
    val page = 1
    val period = TimeRangeFiltering.LAST_30_DAYS.rawValue
    val username = "sunsetscrob"
    val response = """
      {
        "topartists": {
          "artist": [
            {
              "name": "PassCode",
              "playcount": "5793",
              "mbid": "",
              "url": "https://www.last.fm/music/PassCode",
              "streamable": "0",
              "image": [
                {
                  "#text": "https://lastfm-img2.akamaized.net/i/u/34s/3a6201efff969f30b97d94a3586ec2ba.png",
                  "size": "small"
                },
                {
                  "#text": "https://lastfm-img2.akamaized.net/i/u/64s/3a6201efff969f30b97d94a3586ec2ba.png",
                  "size": "medium"
                },
                {
                  "#text": "https://lastfm-img2.akamaized.net/i/u/174s/3a6201efff969f30b97d94a3586ec2ba.png",
                  "size": "large"
                },
                {
                  "#text": "https://lastfm-img2.akamaized.net/i/u/300x300/3a6201efff969f30b97d94a3586ec2ba.png",
                  "size": "extralarge"
                },
                {
                  "#text": "https://lastfm-img2.akamaized.net/i/u/300x300/3a6201efff969f30b97d94a3586ec2ba.png",
                  "size": "mega"
                }
              ],
              "@attr": {
                "rank": "1"
              }
            },
            {
              "name": "乃木坂46",
              "playcount": "4696",
              "mbid": "466f7b0a-a3e6-435d-8326-b4ef825a609d",
              "url": "https://www.last.fm/music/%E4%B9%83%E6%9C%A8%E5%9D%8246",
              "streamable": "0",
              "image": [
                {
                  "#text": "https://lastfm-img2.akamaized.net/i/u/34s/2ebfe25599f4b44a8be724c09ee96ab1.png",
                  "size": "small"
                },
                {
                  "#text": "https://lastfm-img2.akamaized.net/i/u/64s/2ebfe25599f4b44a8be724c09ee96ab1.png",
                  "size": "medium"
                },
                {
                  "#text": "https://lastfm-img2.akamaized.net/i/u/174s/2ebfe25599f4b44a8be724c09ee96ab1.png",
                  "size": "large"
                },
                {
                  "#text": "https://lastfm-img2.akamaized.net/i/u/300x300/2ebfe25599f4b44a8be724c09ee96ab1.png",
                  "size": "extralarge"
                },
                {
                  "#text": "https://lastfm-img2.akamaized.net/i/u/300x300/2ebfe25599f4b44a8be724c09ee96ab1.png",
                  "size": "mega"
                }
              ],
              "@attr": {
                "rank": "2"
              }
            },
            {
              "name": "Power Music Workout",
              "playcount": "4635",
              "mbid": "58511a44-146a-4ca9-81f8-6415d17bf3c6",
              "url": "https://www.last.fm/music/Power+Music+Workout",
              "streamable": "0",
              "image": [
                {
                  "#text": "https://lastfm-img2.akamaized.net/i/u/34s/f18ea296d19f74a9e9ed2f4b0e01dad4.png",
                  "size": "small"
                },
                {
                  "#text": "https://lastfm-img2.akamaized.net/i/u/64s/f18ea296d19f74a9e9ed2f4b0e01dad4.png",
                  "size": "medium"
                },
                {
                  "#text": "https://lastfm-img2.akamaized.net/i/u/174s/f18ea296d19f74a9e9ed2f4b0e01dad4.png",
                  "size": "large"
                },
                {
                  "#text": "https://lastfm-img2.akamaized.net/i/u/300x300/f18ea296d19f74a9e9ed2f4b0e01dad4.png",
                  "size": "extralarge"
                },
                {
                  "#text": "https://lastfm-img2.akamaized.net/i/u/300x300/f18ea296d19f74a9e9ed2f4b0e01dad4.png",
                  "size": "mega"
                }
              ],
              "@attr": {
                "rank": "3"
              }
            },
            {
              "name": "欅坂46",
              "playcount": "2541",
              "mbid": "",
              "url": "https://www.last.fm/music/%E6%AC%85%E5%9D%8246",
              "streamable": "0",
              "image": [
                {
                  "#text": "https://lastfm-img2.akamaized.net/i/u/34s/2b3fa0a4b740f7dec271d249db6eaec6.png",
                  "size": "small"
                },
                {
                  "#text": "https://lastfm-img2.akamaized.net/i/u/64s/2b3fa0a4b740f7dec271d249db6eaec6.png",
                  "size": "medium"
                },
                {
                  "#text": "https://lastfm-img2.akamaized.net/i/u/174s/2b3fa0a4b740f7dec271d249db6eaec6.png",
                  "size": "large"
                },
                {
                  "#text": "https://lastfm-img2.akamaized.net/i/u/300x300/2b3fa0a4b740f7dec271d249db6eaec6.png",
                  "size": "extralarge"
                },
                {
                  "#text": "https://lastfm-img2.akamaized.net/i/u/300x300/2b3fa0a4b740f7dec271d249db6eaec6.png",
                  "size": "mega"
                }
              ],
              "@attr": {
                "rank": "4"
              }
            },
            {
              "name": "[Alexandros]",
              "playcount": "384",
              "mbid": "",
              "url": "https://www.last.fm/music/%5BAlexandros%5D",
              "streamable": "0",
              "image": [
                {
                  "#text": "https://lastfm-img2.akamaized.net/i/u/34s/905dd224c606c424e512b554c0d8c957.png",
                  "size": "small"
                },
                {
                  "#text": "https://lastfm-img2.akamaized.net/i/u/64s/905dd224c606c424e512b554c0d8c957.png",
                  "size": "medium"
                },
                {
                  "#text": "https://lastfm-img2.akamaized.net/i/u/174s/905dd224c606c424e512b554c0d8c957.png",
                  "size": "large"
                },
                {
                  "#text": "https://lastfm-img2.akamaized.net/i/u/300x300/905dd224c606c424e512b554c0d8c957.png",
                  "size": "extralarge"
                },
                {
                  "#text": "https://lastfm-img2.akamaized.net/i/u/300x300/905dd224c606c424e512b554c0d8c957.png",
                  "size": "mega"
                }
              ],
              "@attr": {
                "rank": "5"
              }
            }
          ],
          "@attr": {
            "user": "rhythnn",
            "page": "1",
            "perPage": "5",
            "totalPages": "24",
            "total": "118"
          }
        }
      }
    """.trimIndent()

    val mockEngine = MockEngine { request ->
      request.url.fullPath shouldBe "/2.0/?method=user.getTopArtists&format=json&limit=${limit}&page=${page}&period=${period}&user=${username}"
      request.method shouldBe HttpMethod.Get

      respond(
        content = ByteReadChannel(response),
        status = HttpStatusCode.OK,
        headers = headersOf(HttpHeaders.ContentType, "application/json")
      )
    }
    val artworkDatastore = mockk<ArtworkDataStore>(relaxed = true)
    val lastfmService = LastFmService(
      LastFmHttpClient.create(mockEngine)
    )
    val repository = TopArtistsRepositoryImpl(lastfmService, artworkDatastore)

    it("should parse as TopArtists") {
      repository.fetchTopArtists(
        page = page,
        timeRangeFiltering = TimeRangeFiltering.LAST_30_DAYS,
        username = username
      )
        .test {
          awaitItem().isNotEmpty() shouldBe true
          awaitComplete()
        }
    }
  }
})
