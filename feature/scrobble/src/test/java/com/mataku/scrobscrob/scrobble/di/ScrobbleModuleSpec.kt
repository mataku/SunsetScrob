package com.mataku.scrobscrob.scrobble.di

import io.kotest.core.spec.style.DescribeSpec
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify

class ScrobbleModuleSpec : DescribeSpec({
  @OptIn(KoinExperimentalAPI::class)
  describe("verify") {
    it("should pass") {
      scrobbleModule.verify()
    }
  }
})
