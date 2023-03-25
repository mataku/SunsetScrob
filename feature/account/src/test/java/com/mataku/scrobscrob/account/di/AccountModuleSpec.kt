package com.mataku.scrobscrob.account.di

import io.kotest.core.spec.style.DescribeSpec
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify

@OptIn(KoinExperimentalAPI::class)
class AccountModuleSpec : DescribeSpec({

  describe("verify") {
    it("should pass") {
      accountModule.verify()
    }
  }
})
