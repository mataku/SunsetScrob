package com.mataku.scrobscrob.app.di

import android.content.Context
import io.kotest.core.spec.style.DescribeSpec
import io.ktor.client.HttpClient
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify

class AppModuleSpec : DescribeSpec({
  @OptIn(KoinExperimentalAPI::class)
  describe("verify") {
    it("should pass") {
      appModule.verify(
        extraTypes = listOf(
          HttpClient::class,
          Context::class
        )
      )
    }
  }
})
