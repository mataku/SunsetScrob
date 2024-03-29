package com.mataku.scrobscrob.data.repository

import app.cash.turbine.test
import com.mataku.scrobscrob.core.entity.Stats
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

class ArtistRepositorySpec : DescribeSpec({
  extension(com.mataku.scrobscrob.test_helper.unit.CoroutinesListener())

  describe("artistInfo") {
    val response = """
     {
       "artist": {
         "name": "Nayeon",
         "url": "https://www.last.fm/music/Nayeon",
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
           },
           {
             "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/2a96cbd8b46e442fc41c2b86b821562f.png",
             "size": ""
           }
         ],
         "streamable": "0",
         "ontour": "0",
         "stats": {
           "listeners": "384242",
           "playcount": "15045020"
         },
         "similar": {
           "artist": []
         },
         "tags": {
           "tag": [
             {
               "name": "k-pop",
               "url": "https://www.last.fm/tag/k-pop"
             },
             {
               "name": "better than selena gomez",
               "url": "https://www.last.fm/tag/better+than+selena+gomez"
             },
             {
               "name": "pop",
               "url": "https://www.last.fm/tag/pop"
             },
             {
               "name": "female vocalists",
               "url": "https://www.last.fm/tag/female+vocalists"
             },
             {
               "name": "twice",
               "url": "https://www.last.fm/tag/twice"
             }
           ]
         },
         "bio": {
           "links": {
             "link": {
               "#text": "",
               "rel": "original",
               "href": "https://last.fm/music/Nayeon/+wiki"
             }
           },
           "published": "16 Apr 2018, 21:15",
           "summary": "Im Na-yeon (Hangul: 임나연; born in September 22, 1995), known mononymously as Nayeon (Hangul: 나연), is a South Korean singer, dancer and songwriter. She is the lead vocalist of the girl group  TWICE, where she debuted on October 20, 2015, with the mini album The Story Begins.\n\nShe made her solo debut on June 24, 2022, with the mini album IM NAYEON, making her the first TWICE member to debut as a soloist. <a href=\"https://www.last.fm/music/Nayeon\">Read more on Last.fm</a>",
           "content": "Im Na-yeon (Hangul: 임나연; born in September 22, 1995), known mononymously as Nayeon (Hangul: 나연), is a South Korean singer, dancer and songwriter. She is the lead vocalist of the girl group  TWICE, where she debuted on October 20, 2015, with the mini album The Story Begins.\n\nShe made her solo debut on June 24, 2022, with the mini album IM NAYEON, making her the first TWICE member to debut as a soloist. <a href=\"https://www.last.fm/music/Nayeon\">Read more on Last.fm</a>. User-contributed text is available under the Creative Commons By-SA License; additional terms may apply."
         }
       }
     }
    """.trimIndent()
    val mockEngine = MockEngine { request ->
      request.url.fullPath shouldBe "/2.0/?method=artist.getInfo&format=json&artist=Nayeon"
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
    it("should parse as ArtistInfo") {
      val repository = ArtistRepositoryImpl(lastFmService = lastfmService)
      repository.artistInfo(name = "Nayeon")
        .test {
          awaitItem().let {
            it.name shouldBe "Nayeon"
            it.images.isNotEmpty() shouldBe true
            it.url shouldBe "https://www.last.fm/music/Nayeon"
            it.tags.isNotEmpty() shouldBe true
            it.stats shouldBe Stats(listeners = "384242", playCount = "15045020")
          }
          awaitComplete()
        }
    }
  }

  describe("topAlbums") {
    it("should parse as List<TopAlbumInfo>") {
      val response = """
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
        request.url.fullPath shouldBe "/2.0/?method=artist.gettopalbums&format=json&artist=aespa&page=1&limit=1"
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
      val repository = ArtistRepositoryImpl(lastfmService)
      repository.topAlbums(
        name = "aespa",
        page = 1,
        limit = 1
      )
        .test {
          awaitItem().isNotEmpty() shouldBe true
          awaitComplete()
        }
    }
  }
})
