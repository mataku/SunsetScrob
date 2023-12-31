package com.mataku.scrobscrob.data.repository

import app.cash.turbine.test
import com.mataku.scrobscrob.core.entity.NowPlayingTrackEntity
import com.mataku.scrobscrob.data.api.LastFmHttpClient
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.db.SessionKeyDataStore
import com.mataku.scrobscrob.data.db.UsernameDataStore
import io.kotest.assertions.fail
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
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll

class ScrobbleRepositorySpec : DescribeSpec({
  val usernameDataStore = mockk<UsernameDataStore>()
  val sessionKeyDataStore = mockk<SessionKeyDataStore>()
  val username = "sunsetscrob"
  val sessionKey = "sessionkey"

  beforeSpec {
    coEvery {
      usernameDataStore.username()
    }.returns(username)
    coEvery {
      sessionKeyDataStore.sessionKey()
    }.returns(sessionKey)
  }

  afterSpec {
    unmockkAll()
  }

  describe("recentTracks") {
    it("should parse as RecentTracks") {
      val response = """
        {
          "recenttracks": {
            "track": [
              {
                "artist": {
                  "mbid": "b51c672b-85e0-48fe-8648-470a2422229f",
                  "#text": "aespa"
                },
                "streamable": "0",
                "image": [
                  {
                    "size": "small",
                    "#text": "https://lastfm.freetls.fastly.net/i/u/34s/07bc2400d02a125e7b1ef0858ca57d71.jpg"
                  },
                  {
                    "size": "medium",
                    "#text": "https://lastfm.freetls.fastly.net/i/u/64s/07bc2400d02a125e7b1ef0858ca57d71.jpg"
                  },
                  {
                    "size": "large",
                    "#text": "https://lastfm.freetls.fastly.net/i/u/174s/07bc2400d02a125e7b1ef0858ca57d71.jpg"
                  },
                  {
                    "size": "extralarge",
                    "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/07bc2400d02a125e7b1ef0858ca57d71.jpg"
                  }
                ],
                "mbid": "f758907c-a06b-4187-bba4-f8ea9790e156",
                "album": {
                  "mbid": "",
                  "#text": "Drama - The 4th Mini Album"
                },
                "name": "Drama",
                "url": "https://www.last.fm/music/aespa/_/Drama",
                "date": {
                  "uts": "1703951436",
                  "#text": "30 Dec 2023, 15:50"
                }
              }
            ],
            "@attr": {
              "user": "matakucom",
              "totalPages": "99951",
              "page": "1",
              "perPage": "1",
              "total": "99951"
            }
          }
        }
      """.trimIndent()
      val page = 1
      val mockEngine = MockEngine { request ->
        request.url.fullPath shouldBe "/2.0/?method=user.getrecenttracks&format=json&user=${username}&limit=50&page=${page}"
        request.method shouldBe HttpMethod.Get

        respond(
          content = ByteReadChannel(response),
          status = HttpStatusCode.OK,
          headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
      }
      val lastfmService = LastFmService(
        LastFmHttpClient.create(mockEngine)
      )
      val repository = ScrobbleRepositoryImpl(
        lastfmService,
        usernameDataStore,
        sessionKeyDataStore
      )
      repository.recentTracks(page = page)
        .test {
          awaitItem().let {
            it.size shouldBe 1
            it[0].let { track ->
              track.artistName shouldBe "aespa"
              track.albumName shouldBe "Drama - The 4th Mini Album"
              track.images.isNotEmpty() shouldBe true
              track.name shouldBe "Drama"
              track.url shouldBe "https://www.last.fm/music/aespa/_/Drama"
              track.date shouldBe "30 Dec 2023, 15:50"
            }
          }
          awaitComplete()
        }
    }
  }

  describe("scrobble") {
    context("session key is null") {
      coEvery {
        sessionKeyDataStore.sessionKey()
      }.returns(null)

      val mockEngine = MockEngine {
        fail("unexpected request")
      }
      val lastfmService = LastFmService(
        LastFmHttpClient.create(mockEngine)
      )
      val repository = ScrobbleRepositoryImpl(
        lastfmService,
        usernameDataStore,
        sessionKeyDataStore
      )
      it("should return with accepted: false") {
        repository.scrobble(mockk())
          .test {
            awaitItem().accepted shouldBe false
            awaitComplete()
          }
      }
    }

    context("session key is empty") {
      coEvery {
        sessionKeyDataStore.sessionKey()
      }.returns("")

      val mockEngine = MockEngine {
        fail("unexpected request")
      }
      val lastfmService = LastFmService(
        LastFmHttpClient.create(mockEngine)
      )
      val repository = ScrobbleRepositoryImpl(
        lastfmService,
        usernameDataStore,
        sessionKeyDataStore
      )
      it("should return with accepted: false") {
        repository.scrobble(mockk())
          .test {
            awaitItem().accepted shouldBe false
            awaitComplete()
          }
      }
    }

    context("track is not over scrobble point") {
      val mockEngine = MockEngine {
        fail("unexpected request")
      }
      val lastfmService = LastFmService(
        LastFmHttpClient.create(mockEngine)
      )
      val repository = ScrobbleRepositoryImpl(
        lastfmService,
        usernameDataStore,
        sessionKeyDataStore
      )
      val track = mockk<NowPlayingTrackEntity>()
      every {
        track.overScrobblePoint()
      }.returns(false)

      it("should return with accepted: false") {
        repository.scrobble(track)
          .test {
            awaitItem().accepted shouldBe false
            awaitComplete()
          }
      }
    }
  }

  context("track is over scrobble point") {
    val response = """
      {
        "scrobbles": {
          "@attr": {
            "accepted": 1,
            "ignored": 0
          },
          "scrobble": {
            "artist": {
              "corrected": "0",
              "#text": "Power Music Workout"
            },
            "ignoredMessage": {
              "code": "0",
              "#text": ""
            },
            "albumArtist": {
              "corrected": "0",
              "#text": ""
            },
            "timestamp": "1502971625",
            "album": {
              "corrected": "0",
              "#text": "55 Smash Hits! - Running Remixes, Vol. 3"
            },
            "track": {
              "corrected": "0",
              "#text": "See You Again (Workout Mix)"
            }
          }
        }
      }
    """.trimIndent()
    val artistName = "Power Music Workout"
    val albumName = "Smash Hits"
    val trackName = "See You Again"
    val track = mockk<NowPlayingTrackEntity>(relaxed = true)
    every {
      track.overScrobblePoint()
    }.returns(true)
    every {
      track.artistName
    }.returns(artistName)
    every {
      track.albumName
    }.returns(albumName)
    every {
      track.trackName
    }.returns(trackName)
    every {
      track.timeStamp
    }.returns(300000L)
   
    val mockEngine = MockEngine { request ->
      request.method shouldBe HttpMethod.Post

      respond(
        content = ByteReadChannel(response),
        status = HttpStatusCode.OK,
        headers = headersOf(HttpHeaders.ContentType, "application/json")
      )
    }
    val lastfmService = LastFmService(
      LastFmHttpClient.create(mockEngine)
    )
    coEvery {
      sessionKeyDataStore.sessionKey()
    }.returns(sessionKey)

    val repository = ScrobbleRepositoryImpl(
      lastfmService,
      usernameDataStore,
      sessionKeyDataStore
    )

    it("should return with accepted: true") {
      repository.scrobble(track)
        .test {
          awaitItem().accepted shouldBe true
          awaitComplete()
        }
    }
  }
})
