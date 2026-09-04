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

class UserLovedTracksEndpointSpec : DescribeSpec({
  describe("UserLovedTracksEndpoint") {
    it("issues GET user.getlovedtracks and decodes the response") {
      val username = "matakucom"
      val page = "1"
      val rawJson = """
        {
          "lovedtracks": {
            "track": [
              {
                "artist": {
                  "url": "https://www.last.fm/music/aespa",
                  "name": "aespa"
                },
                "image": [
                  {
                    "size": "extralarge",
                    "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/5fd2847728aa1c914250ca324cb501bf.jpg"
                  }
                ],
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
              "totalPages": "12",
              "page": "1",
              "perPage": "20",
              "total": "234"
            }
          }
        }
      """.trimIndent()

      val mockEngine = MockEngine { request ->
        request.url.fullPath shouldBe
          "/2.0/?method=user.getlovedtracks&format=json&limit=20&page=$page&user=$username"
        request.method shouldBe HttpMethod.Get
        respond(
          content = ByteReadChannel(rawJson),
          status = HttpStatusCode.OK,
          headers = headersOf(HttpHeaders.ContentType, "application/json"),
        )
      }
      val service = LastFmServiceImpl(mockEngine)
      val endpoint = UserLovedTracksEndpoint(
        params = mapOf(
          "limit" to "20",
          "page" to page,
          "user" to username,
        ),
      )

      val response = service.request(endpoint)

      response.lovedTracks.tracks.size shouldBe 1
      response.lovedTracks.tracks[0].name shouldBe "Drama"
      response.lovedTracks.tracks[0].artist.name shouldBe "aespa"
      response.lovedTracks.tracks[0].url shouldBe "https://www.last.fm/music/aespa/_/Drama"
      response.lovedTracks.tracks[0].date?.date shouldBe "30 Dec 2023, 15:50"
      response.lovedTracks.attr.totalPages shouldBe "12"
    }
  }
})
