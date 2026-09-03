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
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel

class ScrobbleEndpointSpec : DescribeSpec({
  describe("ScrobbleEndpoint") {
    it("issues POST track.scrobble and decodes the response") {
      val rawJson = """
        {
          "scrobbles": {
            "@attr": {
              "accepted": 1,
              "ignored": 0
            },
            "scrobble": {
              "artist": {
                "corrected": "0",
                "#text": "Power Music Workout"
              },
              "ignoredMessage": {
                "code": "0",
                "#text": ""
              },
              "albumArtist": {
                "corrected": "0",
                "#text": ""
              },
              "timestamp": "1502971625",
              "album": {
                "corrected": "0",
                "#text": "55 Smash Hits! - Running Remixes, Vol. 3"
              },
              "track": {
                "corrected": "0",
                "#text": "See You Again (Workout Mix)"
              }
            }
          }
        }
      """.trimIndent()

      val mockEngine = MockEngine { request ->
        request.method shouldBe HttpMethod.Post
        respond(
          content = ByteReadChannel(rawJson),
          status = HttpStatusCode.OK,
          headers = headersOf(HttpHeaders.ContentType, "application/json"),
        )
      }
      val service = LastFmServiceImpl(mockEngine)
      val endpoint = ScrobbleEndpoint(
        params = mapOf(
          "album[0]" to "55 Smash Hits! - Running Remixes, Vol. 3",
          "artist[0]" to "Power Music Workout",
          "sk" to "sessionkey",
          "timestamp[0]" to "1502971625",
          "track[0]" to "See You Again (Workout Mix)",
          "api_sig" to "deadbeef",
        ),
      )

      val response = service.request(endpoint)

      response.scrobbleResult.attr.accepted shouldBe 1
      response.scrobbleResult.attr.ignored shouldBe 0
      response.scrobbleResult.scrobble.track.name shouldBe "See You Again (Workout Mix)"
      response.scrobbleResult.scrobble.artist.name shouldBe "Power Music Workout"
    }
  }
})
