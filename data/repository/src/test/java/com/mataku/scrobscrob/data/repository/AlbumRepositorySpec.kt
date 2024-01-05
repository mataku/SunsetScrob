package com.mataku.scrobscrob.data.repository

import app.cash.turbine.test
import com.mataku.scrobscrob.core.entity.TimeRangeFiltering
import com.mataku.scrobscrob.data.api.LastFmHttpClient
import com.mataku.scrobscrob.data.api.LastFmService
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

class AlbumRepositorySpec : DescribeSpec({
  describe("fetchTopAlbums") {
    val page = 1
    val username = "sunsetscrob"
    val timeRangeFiltering = TimeRangeFiltering.LAST_30_DAYS
    it("should parse as TopAlbums") {
      val response = """
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
                  {
                    "size": "small",
                    "#text": "https://lastfm.freetls.fastly.net/i/u/34s/580662fd9ffce7415f00c9130fc3d816.jpg"
                  },
                  {
                    "size": "medium",
                    "#text": "https://lastfm.freetls.fastly.net/i/u/64s/580662fd9ffce7415f00c9130fc3d816.jpg"
                  },
                  {
                    "size": "large",
                    "#text": "https://lastfm.freetls.fastly.net/i/u/174s/580662fd9ffce7415f00c9130fc3d816.jpg"
                  },
                  {
                    "size": "extralarge",
                    "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/580662fd9ffce7415f00c9130fc3d816.jpg"
                  }
                ],
                "mbid": "0ef5b9ba-3206-4472-a0ed-b891f6db1693",
                "url": "https://www.last.fm/music/PassCode/ZENITH",
                "playcount": "4118",
                "@attr": {
                  "rank": "1"
                },
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
        request.url.fullPath shouldBe "/2.0/?method=user.getTopAlbums&format=json&limit=20&page=${page}&period=${timeRangeFiltering.rawValue}&user=${username}"
        request.method shouldBe HttpMethod.Get
        respond(
          content = ByteReadChannel(response),
          status = HttpStatusCode.OK,
          headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
      }
      val lastFmService = LastFmService(
        LastFmHttpClient.create(mockEngine)
      )
      val repository = AlbumRepositoryImpl(lastFmService)
      repository.fetchTopAlbums(
        page = page,
        username = username,
        timeRangeFiltering = timeRangeFiltering
      ).test {
        awaitItem().let {
          it.size shouldBe 1
          it[0].let { albumInfo ->
            albumInfo.artist shouldBe "PassCode"
            albumInfo.title shouldBe "ZENITH"
            albumInfo.imageList.isNotEmpty() shouldBe true
            albumInfo.playCount shouldBe "4118"
            albumInfo.url shouldBe "https://www.last.fm/music/PassCode/ZENITH"
          }
        }
        awaitComplete()
      }
    }
  }

  describe("albumInfo") {
    val albumInfoRawJson = """
      {
        "album": {
          "artist": "aespa",
          "mbid": "ebd108fe-3c3f-4eb8-ac58-89fea95016b8",
          "tags": {
            "tag": {
              "url": "https://www.last.fm/tag/2023",
              "name": "2023"
            }
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
    val albumName = "Drama"
    val artistName = "aespa"
    it("should parse correctly") {
      val mockEngine = MockEngine { request ->
        request.url.fullPath shouldBe "/2.0/?method=album.getinfo&format=json&album=${albumName}&artist=${artistName}"
        request.method shouldBe HttpMethod.Get
        respond(
          content = ByteReadChannel(albumInfoRawJson),
          status = HttpStatusCode.OK,
          headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
      }
      val lastFmService = LastFmService(
        LastFmHttpClient.create(mockEngine)
      )
      val repository = AlbumRepositoryImpl(lastFmService)
      repository.albumInfo(
        albumName = albumName,
        artistName = artistName
      )
        .test {
          awaitItem().let {
            it.artistName shouldBe "aespa"
            it.albumName shouldBe "Drama"
            it.images.isNotEmpty() shouldBe true
            it.tracks.isNotEmpty() shouldBe true
            it.listeners shouldBe "1594"
            it.playCount shouldBe "170051"
          }
          awaitComplete()
        }
    }
  }
})
