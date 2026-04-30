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

class TrackInfoEndpointSpec : DescribeSpec({
  describe("TrackInfoEndpoint") {
    it("issues GET track.getInfo and decodes the response") {
      val artist = "aespa"
      val track = "Drama"
      val username = "matakucom"
      val rawJson = """
        {
          "track": {
            "name": "Drama",
            "url": "https://www.last.fm/music/aespa/_/Drama",
            "duration": 214000,
            "listeners": "120000",
            "playcount": "1500000",
            "userloved": "1",
            "userplaycount": "42",
            "artist": {
              "name": "aespa",
              "url": "https://www.last.fm/music/aespa"
            },
            "album": {
              "artist": "aespa",
              "title": "Drama - The 4th Mini Album",
              "image": [
                {
                  "size": "extralarge",
                  "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/5fd2847728aa1c914250ca324cb501bf.jpg"
                }
              ]
            }
          }
        }
      """.trimIndent()

      val mockEngine = MockEngine { request ->
        request.url.fullPath shouldBe
          "/2.0/?method=track.getInfo&format=json&artist=$artist&track=$track&username=$username"
        request.method shouldBe HttpMethod.Get
        respond(
          content = ByteReadChannel(rawJson),
          status = HttpStatusCode.OK,
          headers = headersOf(HttpHeaders.ContentType, "application/json"),
        )
      }
      val service = LastFmServiceImpl(mockEngine)
      val endpoint = TrackInfoEndpoint(
        params = mapOf(
          "artist" to artist,
          "track" to track,
          "username" to username,
        ),
      )

      val response = service.request(endpoint)

      response.trackInfo.name shouldBe "Drama"
      response.trackInfo.artist.name shouldBe "aespa"
      response.trackInfo.duration shouldBe 214000L
      response.trackInfo.listeners shouldBe "120000"
      response.trackInfo.playCount shouldBe "1500000"
      response.trackInfo.userLoved shouldBe "1"
      response.trackInfo.userPlayCount shouldBe "42"
      response.trackInfo.album?.title shouldBe "Drama - The 4th Mini Album"
    }
  }
})
