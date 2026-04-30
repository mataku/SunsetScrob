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

class AuthMobileSessionEndpointSpec : DescribeSpec({
  describe("AuthMobileSessionEndpoint") {
    it("issues POST auth.getMobileSession and decodes the response") {
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
          "/2.0/?method=auth.getMobileSession&format=json&username=matakucom&password=secret&api_key=apikey&api_sig=deadbeef"
        request.method shouldBe HttpMethod.Post
        respond(
          content = ByteReadChannel(rawJson),
          status = HttpStatusCode.OK,
          headers = headersOf(HttpHeaders.ContentType, "application/json"),
        )
      }
      val service = LastFmServiceImpl(mockEngine)
      val endpoint = AuthMobileSessionEndpoint(
        params = mapOf(
          "username" to "matakucom",
          "password" to "secret",
          "api_key" to "apikey",
          "api_sig" to "deadbeef",
        ),
      )

      val response = service.request(endpoint)

      response.mobileSession.name shouldBe "matakucom"
      response.mobileSession.key shouldBe "abcdef0123456789"
    }
  }
})
