package com.mataku.scrobscrob.account.di

import io.kotest.core.spec.style.DescribeSpec
import org.koin.test.verify.verify

class AccountModuleSpec : DescribeSpec({

  describe("verify") {
    it("should pass") {
      accountModule.verify()
    }
  }
})
