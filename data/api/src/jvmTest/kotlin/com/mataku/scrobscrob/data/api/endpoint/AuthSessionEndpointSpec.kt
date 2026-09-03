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

class AuthSessionEndpointSpec : DescribeSpec({
  describe("AuthSessionEndpoint") {
    it("issues GET auth.getSession and decodes the response") {
      val rawJson = """
        {
          "session": {
            "name": "matakucom",
            "key": "abcdef0123456789",
            "subscriber": 0
          }
        }
      """.trimIndent()

      val mockEngine = MockEngine { request ->
        request.url.fullPath shouldBe
          "/2.0/?method=auth.getSession&format=json&token=tok123&api_key=apikey&api_sig=deadbeef"
        request.method shouldBe HttpMethod.Get
        respond(
          content = ByteReadChannel(rawJson),
          status = HttpStatusCode.OK,
          headers = headersOf(HttpHeaders.ContentType, "application/json"),
        )
      }
      val service = LastFmServiceImpl(mockEngine)
      val endpoint = AuthSessionEndpoint(
        params = mapOf(
          "token" to "tok123",
          "api_key" to "apikey",
          "api_sig" to "deadbeef",
        ),
      )

      val response = service.request(endpoint)

      response.session.name shouldBe "matakucom"
      response.session.key shouldBe "abcdef0123456789"
    }
  }
})
