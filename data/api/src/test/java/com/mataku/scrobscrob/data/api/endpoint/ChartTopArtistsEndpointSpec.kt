package com.mataku.scrobscrob.data.api.endpoint

import com.mataku.scrobscrob.data.api.LastFmServiceImpl
import com.mataku.scrobscrob.data.api.request
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.fullPath
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel

class ChartTopArtistsEndpointSpec : DescribeSpec({
  describe("ChartTopArtistsEndpoint") {
    it("issues GET chart.gettopartists and decodes the response") {
      val page = 1
      val rawJson = """
        {
          "artists": {
            "artist": [
              {
                "name": "The Weeknd",
                "playcount": "578258095",
                "listeners": "3680672",
                "mbid": "c8b03190-306c-4120-bb0b-6f2ebfc06ea9",
                "url": "https://www.last.fm/music/The+Weeknd",
                "streamable": "0",
                "image": [
                  {
                    "#text": "https://lastfm.freetls.fastly.net/i/u/34s/2a96cbd8b46e442fc41c2b86b821562f.png",
                    "size": "small"
                  },
                  {
                    "#text": "https://lastfm.freetls.fastly.net/i/u/64s/2a96cbd8b46e442fc41c2b86b821562f.png",
                    "size": "medium"
                  },
                  {
                    "#text": "https://lastfm.freetls.fastly.net/i/u/174s/2a96cbd8b46e442fc41c2b86b821562f.png",
                    "size": "large"
                  },
                  {
                    "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/2a96cbd8b46e442fc41c2b86b821562f.png",
                    "size": "extralarge"
                  },
                  {
                    "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/2a96cbd8b46e442fc41c2b86b821562f.png",
                    "size": "mega"
                  }
                ]
              }
            ],
            "@attr": {
              "page": "1",
              "perPage": "1",
              "totalPages": "5749773",
              "total": "5749773"
            }
          }
        }
      """.trimIndent()

      val mockEngine = MockEngine { request ->
        request.url.fullPath shouldBe "/2.0/?method=chart.gettopartists&format=json&limit=10&page=$page"
        request.method shouldBe HttpMethod.Get
        respond(
          content = ByteReadChannel(rawJson),
          status = HttpStatusCode.OK,
          headers = headersOf(HttpHeaders.ContentType, "application/json"),
        )
      }
      val service = LastFmServiceImpl(mockEngine)
      val endpoint = ChartTopArtistsEndpoint(
        params = mapOf(
          "limit" to "10",
          "page" to page.toString(),
        ),
      )

      val response = service.request(endpoint)

      response.chartTopArtistsBody.topArtists.size shouldBe 1
      response.chartTopArtistsBody.topArtists[0].name shouldBe "The Weeknd"
      response.chartTopArtistsBody.topArtists[0].playCount shouldBe "578258095"
      response.chartTopArtistsBody.topArtists[0].listeners shouldBe "3680672"
      response.chartTopArtistsBody.topArtists[0].url shouldBe "https://www.last.fm/music/The+Weeknd"
      response.chartTopArtistsBody.topArtists[0].imageList.shouldNotBeEmpty()
      response.chartTopArtistsBody.pagingAttrBody.totalPages shouldBe "5749773"
    }
  }
})
