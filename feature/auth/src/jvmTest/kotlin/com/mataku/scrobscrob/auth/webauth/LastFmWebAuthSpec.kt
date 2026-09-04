package com.mataku.scrobscrob.auth.webauth

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

class LastFmWebAuthSpec : DescribeSpec({
  describe("#tokenFromCallback") {
    it("returns the token for the registered callback URL") {
      LastFmWebAuth.tokenFromCallback("https://sunsetscrob.mataku.com/auth/lastfm?token=abc123") shouldBe "abc123"
    }

    it("returns the token when other query parameters are present") {
      LastFmWebAuth.tokenFromCallback("https://sunsetscrob.mataku.com/auth/lastfm?foo=bar&token=abc123") shouldBe "abc123"
    }

    it("returns null when the token is missing") {
      LastFmWebAuth.tokenFromCallback("https://sunsetscrob.mataku.com/auth/lastfm").shouldBeNull()
    }

    it("returns null when the token is blank") {
      LastFmWebAuth.tokenFromCallback("https://sunsetscrob.mataku.com/auth/lastfm?token=").shouldBeNull()
    }

    it("returns null for a different host") {
      LastFmWebAuth.tokenFromCallback("https://evil.example.com/auth/lastfm?token=abc123").shouldBeNull()
    }

    it("returns null for a different path") {
      LastFmWebAuth.tokenFromCallback("https://sunsetscrob.mataku.com/other?token=abc123").shouldBeNull()
    }

    it("returns null for an unparsable URL") {
      LastFmWebAuth.tokenFromCallback("not a url").shouldBeNull()
    }
  }
})
