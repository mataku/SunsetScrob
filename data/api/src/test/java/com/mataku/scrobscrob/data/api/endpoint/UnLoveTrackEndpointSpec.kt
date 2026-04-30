package com.mataku.scrobscrob.data.api.endpoint

import com.mataku.scrobscrob.data.api.LastFmServiceImpl
import com.mataku.scrobscrob.data.api.request
import io.kotest.assertions.throwables.shouldNotThrowAny
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

class UnLoveTrackEndpointSpec : DescribeSpec({
  describe("UnLoveTrackEndpoint") {
    it("issues POST track.unlove with the expected query params") {
      val mockEngine = MockEngine { request ->
        request.url.fullPath shouldBe
          "/2.0/?method=track.unlove&format=json&artist=aespa&track=Drama&sk=sessionkey&api_key=apikey&api_sig=deadbeef"
        request.method shouldBe HttpMethod.Post
        respond(
          content = ByteReadChannel("{}"),
          status = HttpStatusCode.OK,
          headers = headersOf(HttpHeaders.ContentType, "application/json"),
        )
      }
      val service = LastFmServiceImpl(mockEngine)
      val endpoint = UnLoveTrackEndpoint(
        params = mapOf(
          "artist" to "aespa",
          "track" to "Drama",
          "sk" to "sessionkey",
          "api_key" to "apikey",
          "api_sig" to "deadbeef",
        ),
      )

      shouldNotThrowAny { service.request(endpoint) }
    }
  }
})
