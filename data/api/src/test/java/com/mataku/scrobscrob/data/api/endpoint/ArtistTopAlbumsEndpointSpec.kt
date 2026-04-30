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

class ArtistTopAlbumsEndpointSpec : DescribeSpec({
  describe("ArtistTopAlbumsEndpoint") {
    it("issues GET artist.gettopalbums and decodes the response") {
      val artistName = "aespa"
      val page = 1
      val limit = 1
      val rawJson = """
        {
          "topalbums": {
            "album": [
              {
                "name": "SAVAGE - The 1st Mini Album",
                "playcount": 20996309,
                "url": "https://www.last.fm/music/aespa/SAVAGE+-+The+1st+Mini+Album",
                "artist": {
                  "name": "aespa",
                  "url": "https://www.last.fm/music/aespa"
                },
                "image": [
                  {
                    "#text": "https://lastfm.freetls.fastly.net/i/u/34s/9686de538a7ca3b967de4cc7e76e316b.png",
                    "size": "small"
                  },
                  {
                    "#text": "https://lastfm.freetls.fastly.net/i/u/64s/9686de538a7ca3b967de4cc7e76e316b.png",
                    "size": "medium"
                  },
                  {
                    "#text": "https://lastfm.freetls.fastly.net/i/u/174s/9686de538a7ca3b967de4cc7e76e316b.png",
                    "size": "large"
                  },
                  {
                    "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/9686de538a7ca3b967de4cc7e76e316b.png",
                    "size": "extralarge"
                  }
                ]
              }
            ],
            "@attr": {
              "artist": "aespa",
              "page": "1",
              "perPage": "1",
              "totalPages": "3024",
              "total": "3024"
            }
          }
        }
      """.trimIndent()

      val mockEngine = MockEngine { request ->
        request.url.fullPath shouldBe
          "/2.0/?method=artist.gettopalbums&format=json&artist=$artistName&page=$page&limit=$limit"
        request.method shouldBe HttpMethod.Get
        respond(
          content = ByteReadChannel(rawJson),
          status = HttpStatusCode.OK,
          headers = headersOf(HttpHeaders.ContentType, "application/json"),
        )
      }
      val service = LastFmServiceImpl(mockEngine)
      val endpoint = ArtistTopAlbumsEndpoint(
        params = mapOf(
          "artist" to artistName,
          "page" to page.toString(),
          "limit" to limit.toString(),
        ),
      )

      val response = service.request(endpoint)

      response.topAlbums.albums.size shouldBe 1
      response.topAlbums.albums[0].name shouldBe "SAVAGE - The 1st Mini Album"
      response.topAlbums.albums[0].artist.name shouldBe "aespa"
      response.topAlbums.albums[0].playcount shouldBe "20996309"
      response.topAlbums.pagingAttrBody.totalPages shouldBe "3024"
    }
  }
})
