package com.mataku.scrobscrob.data.api.endpoint

import com.mataku.scrobscrob.core.entity.TimeRangeFiltering
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

class UserTopAlbumsEndpointSpec : DescribeSpec({
  describe("UserTopAlbumsEndpoint") {
    it("issues GET user.getTopAlbums and decodes the response") {
      val page = 1
      val username = "sunsetscrob"
      val timeRangeFiltering = TimeRangeFiltering.LAST_30_DAYS
      val rawJson = """
        {
          "topalbums": {
            "album": [
              {
                "artist": {
                  "url": "https://www.last.fm/music/PassCode",
                  "name": "PassCode",
                  "mbid": "2b875bcd-6f09-4b70-87fc-17d3f17e6097"
                },
                "image": [
                  { "size": "small", "#text": "https://lastfm.freetls.fastly.net/i/u/34s/580662fd9ffce7415f00c9130fc3d816.jpg" },
                  { "size": "medium", "#text": "https://lastfm.freetls.fastly.net/i/u/64s/580662fd9ffce7415f00c9130fc3d816.jpg" },
                  { "size": "large", "#text": "https://lastfm.freetls.fastly.net/i/u/174s/580662fd9ffce7415f00c9130fc3d816.jpg" },
                  { "size": "extralarge", "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/580662fd9ffce7415f00c9130fc3d816.jpg" }
                ],
                "mbid": "0ef5b9ba-3206-4472-a0ed-b891f6db1693",
                "url": "https://www.last.fm/music/PassCode/ZENITH",
                "playcount": "4118",
                "@attr": { "rank": "1" },
                "name": "ZENITH"
              }
            ],
            "@attr": {
              "user": "matakucom",
              "totalPages": "1654",
              "page": "1",
              "perPage": "1",
              "total": "1654"
            }
          }
        }
      """.trimIndent()

      val mockEngine = MockEngine { request ->
        request.url.fullPath shouldBe
          "/2.0/?method=user.getTopAlbums&format=json&limit=20&page=$page&period=${timeRangeFiltering.rawValue}&user=$username"
        request.method shouldBe HttpMethod.Get
        respond(
          content = ByteReadChannel(rawJson),
          status = HttpStatusCode.OK,
          headers = headersOf(HttpHeaders.ContentType, "application/json"),
        )
      }
      val service = LastFmServiceImpl(mockEngine)
      val endpoint = UserTopAlbumsEndpoint(
        params = mapOf(
          "limit" to 20,
          "page" to page,
          "period" to timeRangeFiltering.rawValue,
          "user" to username,
        ),
      )

      val response = service.request(endpoint)

      response.topAlbums.albums.size shouldBe 1
      response.topAlbums.albums[0].name shouldBe "ZENITH"
      response.topAlbums.albums[0].artist.name shouldBe "PassCode"
      response.topAlbums.pagingAttrBody.totalPages shouldBe "1654"
    }
  }
})
