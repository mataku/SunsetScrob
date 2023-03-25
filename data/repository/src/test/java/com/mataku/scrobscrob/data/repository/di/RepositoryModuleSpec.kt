package com.mataku.scrobscrob.data.repository.di

import android.content.Context
import io.kotest.core.spec.style.DescribeSpec
import io.ktor.client.HttpClient
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify

class RepositoryModuleSpec : DescribeSpec({
  @OptIn(KoinExperimentalAPI::class)
  describe("verify") {
    it("should pass") {
      repositoryModule.verify(
        extraTypes = listOf(
          HttpClient::class,
          Context::class
        )
      )
    }
  }
})
