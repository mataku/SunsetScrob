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

class UserInfoEndpointSpec : DescribeSpec({
  describe("UserInfoEndpoint") {
    it("issues GET user.getinfo and decodes the response") {
      val username = "matakucom"
      val rawJson = """
        {
          "user": {
            "name": "matakucom",
            "image": [
              {
                "size": "small",
                "#text": "https://lastfm.freetls.fastly.net/i/u/34s/3605caa7a395e19202c55d55be23cbff.png"
              },
              {
                "size": "extralarge",
                "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/3605caa7a395e19202c55d55be23cbff.png"
              }
            ],
            "url": "https://www.last.fm/user/matakucom",
            "country": "Japan",
            "playcount": "102654",
            "artist_count": "728",
            "track_count": "2296",
            "album_count": "1753"
          }
        }
      """.trimIndent()

      val mockEngine = MockEngine { request ->
        request.url.fullPath shouldBe
          "/2.0/?method=user.getinfo&format=json&user=$username"
        request.method shouldBe HttpMethod.Get
        respond(
          content = ByteReadChannel(rawJson),
          status = HttpStatusCode.OK,
          headers = headersOf(HttpHeaders.ContentType, "application/json"),
        )
      }
      val service = LastFmServiceImpl(mockEngine)
      val endpoint = UserInfoEndpoint(
        params = mapOf("user" to username),
      )

      val response = service.request(endpoint)

      response.userInfo.name shouldBe "matakucom"
      response.userInfo.playCount shouldBe "102654"
      response.userInfo.artistCount shouldBe "728"
      response.userInfo.trackCount shouldBe "2296"
      response.userInfo.albumCount shouldBe "1753"
      response.userInfo.url shouldBe "https://www.last.fm/user/matakucom"
      response.userInfo.imageList?.shouldNotBeEmpty()
    }
  }
})
