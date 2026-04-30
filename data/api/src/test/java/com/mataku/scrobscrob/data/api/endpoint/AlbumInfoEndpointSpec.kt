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

class AlbumInfoEndpointSpec : DescribeSpec({
  describe("AlbumInfoEndpoint") {
    it("issues GET album.getinfo and decodes the response") {
      val albumName = "Drama"
      val artistName = "aespa"
      val rawJson = """
        {
          "album": {
            "artist": "aespa",
            "mbid": "ebd108fe-3c3f-4eb8-ac58-89fea95016b8",
            "tags": {
              "tag": [
                {
                  "url": "https://www.last.fm/tag/rock",
                  "name": "rock"
                },
                {
                  "url": "https://www.last.fm/tag/alternative",
                  "name": "alternative"
                },
                {
                  "url": "https://www.last.fm/tag/britpop",
                  "name": "britpop"
                },
                {
                  "url": "https://www.last.fm/tag/coldplay",
                  "name": "coldplay"
                },
                {
                  "url": "https://www.last.fm/tag/alternative+rock",
                  "name": "alternative rock"
                }
              ]
            },
            "name": "Drama",
            "image": [
              {
                "size": "small",
                "#text": "https://lastfm.freetls.fastly.net/i/u/34s/5fd2847728aa1c914250ca324cb501bf.jpg"
              },
              {
                "size": "medium",
                "#text": "https://lastfm.freetls.fastly.net/i/u/64s/5fd2847728aa1c914250ca324cb501bf.jpg"
              },
              {
                "size": "large",
                "#text": "https://lastfm.freetls.fastly.net/i/u/174s/5fd2847728aa1c914250ca324cb501bf.jpg"
              },
              {
                "size": "extralarge",
                "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/5fd2847728aa1c914250ca324cb501bf.jpg"
              },
              {
                "size": "mega",
                "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/5fd2847728aa1c914250ca324cb501bf.jpg"
              },
              {
                "size": "",
                "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/5fd2847728aa1c914250ca324cb501bf.jpg"
              }
            ],
            "tracks": {
              "track": [
                {
                  "streamable": {
                    "fulltrack": "0",
                    "#text": "0"
                  },
                  "duration": 214,
                  "url": "https://www.last.fm/music/aespa/Drama/Drama",
                  "name": "Drama",
                  "@attr": {
                    "rank": 1
                  },
                  "artist": {
                    "url": "https://www.last.fm/music/aespa",
                    "name": "aespa",
                    "mbid": "b51c672b-85e0-48fe-8648-470a2422229f"
                  }
                },
                {
                  "streamable": {
                    "fulltrack": "0",
                    "#text": "0"
                  },
                  "duration": 175,
                  "url": "https://www.last.fm/music/aespa/Drama/Trick+or+Trick",
                  "name": "Trick or Trick",
                  "@attr": {
                    "rank": 2
                  },
                  "artist": {
                    "url": "https://www.last.fm/music/aespa",
                    "name": "aespa",
                    "mbid": "b51c672b-85e0-48fe-8648-470a2422229f"
                  }
                },
                {
                  "streamable": {
                    "fulltrack": "0",
                    "#text": "0"
                  },
                  "duration": 169,
                  "url": "https://www.last.fm/music/aespa/Drama/Don%27t+Blink",
                  "name": "Don't Blink",
                  "@attr": {
                    "rank": 3
                  },
                  "artist": {
                    "url": "https://www.last.fm/music/aespa",
                    "name": "aespa",
                    "mbid": "b51c672b-85e0-48fe-8648-470a2422229f"
                  }
                },
                {
                  "streamable": {
                    "fulltrack": "0",
                    "#text": "0"
                  },
                  "duration": 198,
                  "url": "https://www.last.fm/music/aespa/Drama/Hot+Air+Balloon",
                  "name": "Hot Air Balloon",
                  "@attr": {
                    "rank": 4
                  },
                  "artist": {
                    "url": "https://www.last.fm/music/aespa",
                    "name": "aespa",
                    "mbid": "b51c672b-85e0-48fe-8648-470a2422229f"
                  }
                },
                {
                  "streamable": {
                    "fulltrack": "0",
                    "#text": "0"
                  },
                  "duration": 189,
                  "url": "https://www.last.fm/music/aespa/Drama/YOLO",
                  "name": "YOLO",
                  "@attr": {
                    "rank": 5
                  },
                  "artist": {
                    "url": "https://www.last.fm/music/aespa",
                    "name": "aespa",
                    "mbid": "b51c672b-85e0-48fe-8648-470a2422229f"
                  }
                },
                {
                  "streamable": {
                    "fulltrack": "0",
                    "#text": "0"
                  },
                  "duration": 203,
                  "url": "https://www.last.fm/music/aespa/Drama/You",
                  "name": "You",
                  "@attr": {
                    "rank": 6
                  },
                  "artist": {
                    "url": "https://www.last.fm/music/aespa",
                    "name": "aespa",
                    "mbid": "b51c672b-85e0-48fe-8648-470a2422229f"
                  }
                }
              ]
            },
            "listeners": "1594",
            "playcount": "170051",
            "url": "https://www.last.fm/music/aespa/Drama"
          }
        }
      """.trimIndent()

      val mockEngine = MockEngine { request ->
        request.url.fullPath shouldBe
          "/2.0/?method=album.getinfo&format=json&album=$albumName&artist=$artistName"
        request.method shouldBe HttpMethod.Get
        respond(
          content = ByteReadChannel(rawJson),
          status = HttpStatusCode.OK,
          headers = headersOf(HttpHeaders.ContentType, "application/json"),
        )
      }
      val service = LastFmServiceImpl(mockEngine)
      val endpoint = AlbumInfoEndpoint(
        params = mapOf("album" to albumName, "artist" to artistName),
      )

      val response = service.request(endpoint)

      response.albumInfoBody.albumName shouldBe "Drama"
      response.albumInfoBody.artistName shouldBe "aespa"
      response.albumInfoBody.images.isNotEmpty() shouldBe true
      response.albumInfoBody.tracks?.tracks?.isNotEmpty() shouldBe true
      response.albumInfoBody.listeners shouldBe "1594"
      response.albumInfoBody.playCount shouldBe "170051"
    }
  }
})
