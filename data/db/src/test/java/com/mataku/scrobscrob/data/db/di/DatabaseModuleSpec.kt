package com.mataku.scrobscrob.data.db.di

import android.content.Context
import io.kotest.core.spec.style.DescribeSpec
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify

class DatabaseModuleSpec : DescribeSpec({
  @OptIn(KoinExperimentalAPI::class)
  describe("verify") {
    it("should pass") {
      databaseModule.verify(
        extraTypes = listOf(
          Context::class
        )
      )
    }
  }
})
