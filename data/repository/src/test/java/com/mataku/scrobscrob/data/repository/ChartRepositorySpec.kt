package com.mataku.scrobscrob.data.repository

import app.cash.turbine.test
import com.mataku.scrobscrob.data.api.LastFmHttpClient
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.test_helper.unit.CoroutinesListener
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

class ChartRepositorySpec : DescribeSpec({
  extension(CoroutinesListener())

  describe("topArtists") {
    val rawResponse = """
      {
        "artists": {
          "artist": [
            {
              "name": "The Weeknd",
              "playcount": "578258095",
              "listeners": "3680672",
              "mbid": "c8b03190-306c-4120-bb0b-6f2ebfc06ea9",
              "url": "https://www.last.fm/music/The+Weeknd",
              "streamable": "0",
              "image": [
                {
                  "#text": "https://lastfm.freetls.fastly.net/i/u/34s/2a96cbd8b46e442fc41c2b86b821562f.png",
                  "size": "small"
                },
                {
                  "#text": "https://lastfm.freetls.fastly.net/i/u/64s/2a96cbd8b46e442fc41c2b86b821562f.png",
                  "size": "medium"
                },
                {
                  "#text": "https://lastfm.freetls.fastly.net/i/u/174s/2a96cbd8b46e442fc41c2b86b821562f.png",
                  "size": "large"
                },
                {
                  "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/2a96cbd8b46e442fc41c2b86b821562f.png",
                  "size": "extralarge"
                },
                {
                  "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/2a96cbd8b46e442fc41c2b86b821562f.png",
                  "size": "mega"
                }
              ]
            }
          ],
          "@attr": {
            "page": "1",
            "perPage": "1",
            "totalPages": "5749773",
            "total": "5749773"
          }
        }
      }
    """.trimIndent()
    val page = 1
    val mockEngine = MockEngine { request ->
      request.url.fullPath shouldBe "/2.0/?method=chart.gettopartists&format=json&limit=30&page=1"
      request.method shouldBe HttpMethod.Get

      respond(
        content = ByteReadChannel(rawResponse),
        status = HttpStatusCode.OK,
        headers = headersOf(HttpHeaders.ContentType, "application/json")
      )
    }
    val lastFmService = LastFmService(
      LastFmHttpClient.create(mockEngine)
    )

    it("should parse as ChartTopArtists") {
      val repository = ChartRepositoryImpl(lastFmService)

      repository.topArtists(page)
        .test {
          awaitItem().topArtists.isNotEmpty() shouldBe true
          awaitComplete()
        }
    }
  }

  describe("topTracks") {
    val rawResponse = """
      {
        "tracks": {
          "track": [
            {
              "name": "My Love Mine All Mine",
              "duration": "0",
              "playcount": "16319592",
              "listeners": "843008",
              "mbid": "",
              "url": "https://www.last.fm/music/Mitski/_/My+Love+Mine+All+Mine",
              "streamable": {
                "#text": "0",
                "fulltrack": "0"
              },
              "artist": {
                "name": "Mitski",
                "mbid": "fa58cf24-0e44-421d-8519-8bf461dcfaa5",
                "url": "https://www.last.fm/music/Mitski"
              },
              "image": [
                {
                  "#text": "https://lastfm.freetls.fastly.net/i/u/34s/2a96cbd8b46e442fc41c2b86b821562f.png",
                  "size": "small"
                },
                {
                  "#text": "https://lastfm.freetls.fastly.net/i/u/64s/2a96cbd8b46e442fc41c2b86b821562f.png",
                  "size": "medium"
                },
                {
                  "#text": "https://lastfm.freetls.fastly.net/i/u/174s/2a96cbd8b46e442fc41c2b86b821562f.png",
                  "size": "large"
                },
                {
                  "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/2a96cbd8b46e442fc41c2b86b821562f.png",
                  "size": "extralarge"
                }
              ]
            }
          ],
          "@attr": {
            "page": "1",
            "perPage": "1",
            "totalPages": "37697653",
            "total": "37697653"
          }
        }
      }
    """.trimIndent()
    val page = 1
    val mockEngine = MockEngine { request ->
      request.url.fullPath shouldBe "/2.0/?method=chart.gettoptracks&format=json&limit=30&page=1"
      request.method shouldBe HttpMethod.Get

      respond(
        content = ByteReadChannel(rawResponse),
        status = HttpStatusCode.OK,
        headers = headersOf(HttpHeaders.ContentType, "application/json")
      )
    }
    val lastFmService = LastFmService(
      LastFmHttpClient.create(mockEngine)
    )

    it("should parse as ChartTopTracks") {
      val repository = ChartRepositoryImpl(lastFmService)
      repository.topTracks(page)
        .test {
          awaitItem().topTracks.isNotEmpty() shouldBe true
          awaitComplete()
        }
    }
  }
})
