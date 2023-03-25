package com.mataku.scrobscrob.data.api.di

import io.kotest.core.spec.style.DescribeSpec
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify

class ApiModuleTest : DescribeSpec({
  @OptIn(KoinExperimentalAPI::class)
  describe("verify") {
    it("should pass") {
      apiModule.verify()
    }
  }
})
