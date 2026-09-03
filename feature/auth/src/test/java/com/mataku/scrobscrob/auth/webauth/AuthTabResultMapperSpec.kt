package com.mataku.scrobscrob.auth.webauth

import androidx.browser.auth.AuthTabIntent
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class AuthTabResultMapperSpec : DescribeSpec({
  describe("#mapAuthTabResult") {
    it("returns Canceled for RESULT_CANCELED") {
      mapAuthTabResult(AuthTabIntent.RESULT_CANCELED, null) shouldBe LastFmWebAuthResult.Canceled
    }

    it("returns Failed for RESULT_VERIFICATION_FAILED") {
      mapAuthTabResult(AuthTabIntent.RESULT_VERIFICATION_FAILED, null) shouldBe LastFmWebAuthResult.Failed
    }

    it("returns Failed for RESULT_VERIFICATION_TIMED_OUT") {
      mapAuthTabResult(AuthTabIntent.RESULT_VERIFICATION_TIMED_OUT, null) shouldBe LastFmWebAuthResult.Failed
    }

    it("returns Failed for RESULT_UNKNOWN_CODE") {
      mapAuthTabResult(AuthTabIntent.RESULT_UNKNOWN_CODE, null) shouldBe LastFmWebAuthResult.Failed
    }

    it("returns Success with the token for RESULT_OK and a valid callback URI") {
      mapAuthTabResult(
        AuthTabIntent.RESULT_OK,
        "https://sunsetscrob.mataku.com/auth/lastfm?token=abc123",
      ) shouldBe LastFmWebAuthResult.Success("abc123")
    }

    it("returns Failed for RESULT_OK with a null URI") {
      mapAuthTabResult(AuthTabIntent.RESULT_OK, null) shouldBe LastFmWebAuthResult.Failed
    }

    it("returns Failed for RESULT_OK with a wrong host") {
      mapAuthTabResult(
        AuthTabIntent.RESULT_OK,
        "https://evil.example.com/auth/lastfm?token=abc123",
      ) shouldBe LastFmWebAuthResult.Failed
    }
  }
})
