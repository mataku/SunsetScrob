package com.mataku.scrobscrob.data.api.endpoint

import com.mataku.scrobscrob.data.api.LastFmServiceImpl
import com.mataku.scrobscrob.data.api.request
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

class UserRecentTracksEndpointSpec : DescribeSpec({
  describe("UserRecentTracksEndpoint") {
    it("issues GET user.getrecenttracks and decodes the response") {
      val username = "sunsetscrob"
      val page = 1
      val rawJson = """
        {
          "recenttracks": {
            "track": [
              {
                "artist": {
                  "mbid": "b51c672b-85e0-48fe-8648-470a2422229f",
                  "#text": "aespa"
                },
                "streamable": "0",
                "image": [
                  {
                    "size": "small",
                    "#text": "https://lastfm.freetls.fastly.net/i/u/34s/07bc2400d02a125e7b1ef0858ca57d71.jpg"
                  },
                  {
                    "size": "medium",
                    "#text": "https://lastfm.freetls.fastly.net/i/u/64s/07bc2400d02a125e7b1ef0858ca57d71.jpg"
                  },
                  {
                    "size": "large",
                    "#text": "https://lastfm.freetls.fastly.net/i/u/174s/07bc2400d02a125e7b1ef0858ca57d71.jpg"
                  },
                  {
                    "size": "extralarge",
                    "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/07bc2400d02a125e7b1ef0858ca57d71.jpg"
                  }
                ],
                "mbid": "f758907c-a06b-4187-bba4-f8ea9790e156",
                "album": {
                  "mbid": "",
                  "#text": "Drama - The 4th Mini Album"
                },
                "name": "Drama",
                "url": "https://www.last.fm/music/aespa/_/Drama",
                "date": {
                  "uts": "1703951436",
                  "#text": "30 Dec 2023, 15:50"
                }
              }
            ],
            "@attr": {
              "user": "matakucom",
              "totalPages": "99951",
              "page": "1",
              "perPage": "1",
              "total": "99951"
            }
          }
        }
      """.trimIndent()

      val mockEngine = MockEngine { request ->
        request.url.fullPath shouldBe
          "/2.0/?method=user.getrecenttracks&format=json&user=$username&limit=50&page=$page"
        request.method shouldBe HttpMethod.Get
        respond(
          content = ByteReadChannel(rawJson),
          status = HttpStatusCode.OK,
          headers = headersOf(HttpHeaders.ContentType, "application/json"),
        )
      }
      val service = LastFmServiceImpl(mockEngine)
      val endpoint = UserRecentTracksEndpoint(
        params = mapOf("user" to username, "limit" to 50, "page" to page),
      )

      val response = service.request(endpoint)

      response.recentTracks.tracks.size shouldBe 1
      response.recentTracks.tracks[0].name shouldBe "Drama"
      response.recentTracks.tracks[0].artist.name shouldBe "aespa"
      response.recentTracks.tracks[0].album.name shouldBe "Drama - The 4th Mini Album"
      response.recentTracks.tracks[0].url shouldBe "https://www.last.fm/music/aespa/_/Drama"
      response.recentTracks.tracks[0].date?.date shouldBe "30 Dec 2023, 15:50"
      response.recentTracks.attr.totalPages shouldBe "99951"
    }
  }
})
