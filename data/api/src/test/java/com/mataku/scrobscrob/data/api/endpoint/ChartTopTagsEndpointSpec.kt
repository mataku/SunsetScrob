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

class ChartTopTagsEndpointSpec : DescribeSpec({
  describe("ChartTopTagsEndpoint") {
    it("issues GET chart.gettoptags and decodes the response") {
      val rawJson = """
        {
          "tags": {
            "tag": [
              {
                "name": "rock",
                "url": "https://www.last.fm/tag/rock"
              },
              {
                "name": "pop",
                "url": "https://www.last.fm/tag/pop"
              }
            ],
            "@attr": {
              "page": "1",
              "perPage": "50",
              "totalPages": "1",
              "total": "2"
            }
          }
        }
      """.trimIndent()

      val mockEngine = MockEngine { request ->
        request.url.fullPath shouldBe "/2.0/?method=chart.gettoptags&format=json&limit=10&page=1"
        request.method shouldBe HttpMethod.Get
        respond(
          content = ByteReadChannel(rawJson),
          status = HttpStatusCode.OK,
          headers = headersOf(HttpHeaders.ContentType, "application/json"),
        )
      }
      val service = LastFmServiceImpl(mockEngine)
      val endpoint = ChartTopTagsEndpoint(
        params = mapOf(
          "limit" to "10",
          "page" to "1",
        ),
      )

      val response = service.request(endpoint)

      response.chartTopTagsBody.tagList.size shouldBe 2
      response.chartTopTagsBody.tagList[0].name shouldBe "rock"
      response.chartTopTagsBody.tagList[0].url shouldBe "https://www.last.fm/tag/rock"
      response.chartTopTagsBody.tagList[1].name shouldBe "pop"
    }
  }
})
