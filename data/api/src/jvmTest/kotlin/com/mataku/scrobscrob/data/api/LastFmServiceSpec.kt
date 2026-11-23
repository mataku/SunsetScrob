package com.mataku.scrobscrob.data.api

import com.mataku.scrobscrob.data.api.endpoint.LastFmApiError
import com.mataku.scrobscrob.data.api.endpoint.LoveTrackEndpoint
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel

class LastFmServiceSpec : DescribeSpec({
  val endpoint = LoveTrackEndpoint(params = mapOf("artist" to "aespa", "track" to "Drama"))

  describe("rawRequest") {
    it("sends requests to the Last.fm API host over HTTPS") {
      val mockEngine = MockEngine { request ->
        request.url.protocol shouldBe URLProtocol.HTTPS
        request.url.host shouldBe "ws.audioscrobbler.com"
        respond(
          content = ByteReadChannel("{}"),
          status = HttpStatusCode.OK,
          headers = headersOf(HttpHeaders.ContentType, "application/json"),
        )
      }

      LastFmServiceImpl(mockEngine).request(endpoint)

      mockEngine.requestHistory.size shouldBe 1
    }

    context("a POST fails with a Last.fm error body") {
      it("throws LastFmApiError carrying the message from the body") {
        val mockEngine = MockEngine {
          respond(
            content = ByteReadChannel("""{"message":"Invalid session key","error":9}"""),
            status = HttpStatusCode.Forbidden,
            headers = headersOf(HttpHeaders.ContentType, "application/json"),
          )
        }

        val error = shouldThrow<LastFmApiError> {
          LastFmServiceImpl(mockEngine).request(endpoint)
        }

        error.errorMessage shouldBe "Invalid session key"
      }
    }

    context("a POST fails with a body that is not a Last.fm error") {
      it("throws LastFmApiError with a fallback message") {
        val mockEngine = MockEngine {
          respond(
            content = ByteReadChannel("<html>Bad Gateway</html>"),
            status = HttpStatusCode.BadGateway,
            headers = headersOf(HttpHeaders.ContentType, "text/html"),
          )
        }

        val error = shouldThrow<LastFmApiError> {
          LastFmServiceImpl(mockEngine).request(endpoint)
        }

        error.errorMessage shouldBe "Unknown error"
      }
    }
  }
})
