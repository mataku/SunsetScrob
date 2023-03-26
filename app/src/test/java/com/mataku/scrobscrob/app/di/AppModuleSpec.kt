package com.mataku.scrobscrob.app.di

import android.content.Context
import com.mataku.scrobscrob.account.di.accountModule
import com.mataku.scrobscrob.album.di.albumModule
import com.mataku.scrobscrob.artist.di.artistModule
import com.mataku.scrobscrob.auth.di.authModule
import com.mataku.scrobscrob.data.repository.di.repositoryModule
import com.mataku.scrobscrob.scrobble.di.scrobbleModule
import io.kotest.core.spec.style.DescribeSpec
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.test.verify.verify

class AppModuleSpec : DescribeSpec({
  @OptIn(KoinExperimentalAPI::class)
  describe("verify") {
    it("should pass") {
      val modules = listOf(
        appModule,
        repositoryModule, // includes api module and database module
        accountModule,
        albumModule,
        artistModule,
        authModule,
        scrobbleModule
      )
      module {
        includes(modules)
      }.verify(
        extraTypes = listOf(
          Context::class,
          HttpClientEngine::class,
          HttpClientConfig::class
        )
      )
    }
  }
})
