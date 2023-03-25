package com.mataku.scrobscrob.auth.di

import io.kotest.core.spec.style.DescribeSpec
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify

@OptIn(KoinExperimentalAPI::class)
class AuthModuleSpec : DescribeSpec({

  describe("verify") {
    it("should pass") {
      authModule.verify()
    }
  }
})
