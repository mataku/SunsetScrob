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

class UpdateNowPlayingEndpointSpec : DescribeSpec({
  describe("UpdateNowPlayingEndpoint") {
    it("issues POST track.updateNowPlaying and decodes the response") {
      val rawJson = """
        {
          "nowplaying": {
            "artist": {
              "corrected": "0",
              "#text": "aespa"
            },
            "album": {
              "corrected": "0",
              "#text": "Drama"
            },
            "track": {
              "corrected": "0",
              "#text": "Drama"
            }
          }
        }
      """.trimIndent()

      val mockEngine = MockEngine { request ->
        request.url.fullPath shouldBe
          "/2.0/?method=track.updateNowPlaying&format=json&artist=aespa&track=Drama&album=Drama&sk=sessionkey&api_key=apikey&api_sig=deadbeef"
        request.method shouldBe HttpMethod.Post
        respond(
          content = ByteReadChannel(rawJson),
          status = HttpStatusCode.OK,
          headers = headersOf(HttpHeaders.ContentType, "application/json"),
        )
      }
      val service = LastFmServiceImpl(mockEngine)
      val endpoint = UpdateNowPlayingEndpoint(
        params = mapOf(
          "artist" to "aespa",
          "track" to "Drama",
          "album" to "Drama",
          "sk" to "sessionkey",
          "api_key" to "apikey",
          "api_sig" to "deadbeef",
        ),
      )

      val response = service.request(endpoint)

      response.nowPlaying.artist.text shouldBe "aespa"
      response.nowPlaying.track.text shouldBe "Drama"
      response.nowPlaying.album.text shouldBe "Drama"
    }
  }
})
