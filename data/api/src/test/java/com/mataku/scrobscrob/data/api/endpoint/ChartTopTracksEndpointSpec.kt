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

class ChartTopTracksEndpointSpec : DescribeSpec({
  describe("ChartTopTracksEndpoint") {
    it("issues GET chart.gettoptracks and decodes the response") {
      val page = 1
      val rawJson = """
        {
          "tracks": {
            "track": [
              {
                "name": "My Love Mine All Mine",
                "duration": "0",
                "playcount": "16319592",
                "listeners": "843008",
                "mbid": "",
                "url": "https://www.last.fm/music/Mitski/_/My+Love+Mine+All+Mine",
                "streamable": {
                  "#text": "0",
                  "fulltrack": "0"
                },
                "artist": {
                  "name": "Mitski",
                  "mbid": "fa58cf24-0e44-421d-8519-8bf461dcfaa5",
                  "url": "https://www.last.fm/music/Mitski"
                },
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
                  }
                ]
              }
            ],
            "@attr": {
              "page": "1",
              "perPage": "1",
              "totalPages": "37697653",
              "total": "37697653"
            }
          }
        }
      """.trimIndent()

      val mockEngine = MockEngine { request ->
        request.url.fullPath shouldBe "/2.0/?method=chart.gettoptracks&format=json&limit=10&page=$page"
        request.method shouldBe HttpMethod.Get
        respond(
          content = ByteReadChannel(rawJson),
          status = HttpStatusCode.OK,
          headers = headersOf(HttpHeaders.ContentType, "application/json"),
        )
      }
      val service = LastFmServiceImpl(mockEngine)
      val endpoint = ChartTopTracksEndpoint(
        params = mapOf(
          "limit" to "10",
          "page" to page.toString(),
        ),
      )

      val response = service.request(endpoint)

      response.chartTopTracksBody.topTracks.size shouldBe 1
      response.chartTopTracksBody.topTracks[0].name shouldBe "My Love Mine All Mine"
      response.chartTopTracksBody.topTracks[0].playCount shouldBe "16319592"
      response.chartTopTracksBody.topTracks[0].listeners shouldBe "843008"
      response.chartTopTracksBody.topTracks[0].url shouldBe "https://www.last.fm/music/Mitski/_/My+Love+Mine+All+Mine"
      response.chartTopTracksBody.topTracks[0].artist.name shouldBe "Mitski"
      response.chartTopTracksBody.topTracks[0].imageList.shouldNotBeEmpty()
      response.chartTopTracksBody.pagingAttrBody.totalPages shouldBe "37697653"
    }
  }
})
