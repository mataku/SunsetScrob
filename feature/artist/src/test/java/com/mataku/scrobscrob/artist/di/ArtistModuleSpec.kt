package com.mataku.scrobscrob.artist.di

import io.kotest.core.spec.style.DescribeSpec
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify

@OptIn(KoinExperimentalAPI::class)
class ArtistModuleSpec : DescribeSpec({

  describe("verify") {
    it("should pass") {
      artistModule.verify()
    }
  }
})
