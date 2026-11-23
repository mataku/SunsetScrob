package com.mataku.scrobscrob.data.api.endpoint

import com.mataku.scrobscrob.data.api.LastFmApiCredentials
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import java.security.MessageDigest

class ApiSignatureSpec : DescribeSpec({
  describe("generateApiSig") {
    it("hashes the params sorted by key, then the shared secret") {
      val params = mutableMapOf(
        "track" to "Drama",
        "artist" to "aespa",
        "method" to "track.love",
        "sk" to "sessionkey",
      )

      val signature = ApiSignature.generateApiSig(params)

      signature shouldBe md5Hex(
        "api_key${LastFmApiCredentials.API_KEY}" +
          "artistaespa" +
          "methodtrack.love" +
          "sksessionkey" +
          "trackDrama" +
          LastFmApiCredentials.SHARED_SECRET,
      )
    }

    it("adds api_key to the given params") {
      val params = mutableMapOf("method" to "auth.getSession")

      ApiSignature.generateApiSig(params)

      params shouldContain ("api_key" to LastFmApiCredentials.API_KEY)
    }

    it("signs non-ASCII values as UTF-8") {
      val signature = ApiSignature.generateApiSig(mutableMapOf("artist" to "欅坂46"))

      signature shouldBe md5Hex(
        "api_key${LastFmApiCredentials.API_KEY}artist欅坂46${LastFmApiCredentials.SHARED_SECRET}",
      )
    }

    // A digest byte below 0x10 must keep its leading zero, or the signature comes out shorter than
    // 32 characters and Last.fm rejects it. Any single input may not contain such a byte, so sign
    // enough inputs that some do.
    it("always returns 32 lowercase hex characters") {
      repeat(50) { index ->
        ApiSignature.generateApiSig(mutableMapOf("track" to "track-$index")) shouldMatch "[0-9a-f]{32}"
      }
    }
  }
})

private fun md5Hex(value: String): String =
  MessageDigest.getInstance("MD5")
    .digest(value.toByteArray(Charsets.UTF_8))
    .joinToString("") { "%02x".format(it) }
