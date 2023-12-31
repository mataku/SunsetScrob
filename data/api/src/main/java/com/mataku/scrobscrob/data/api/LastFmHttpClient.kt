package com.mataku.scrobscrob.data.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object LastFmHttpClient {
  fun create(
    engine: HttpClientEngine
  ): HttpClient {
    return HttpClient(engine) {
      install(ContentNegotiation) {
        json(Json {
          isLenient = true
          ignoreUnknownKeys = true
        })
      }
      install(HttpCache)
    }
  }
}
