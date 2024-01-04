package com.mataku.scrobscrob.data.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object LastFmHttpClient {
  private const val LASTFM_HOST = "ws.audioscrobbler.com"

  fun create(
    engine: HttpClientEngine,
  ): HttpClient {
    return HttpClient(engine) {
      install(ContentNegotiation) {
        json(Json {
          isLenient = true
          ignoreUnknownKeys = true
        })
      }
      defaultRequest {
        url {
          protocol = URLProtocol.HTTPS
          host = LASTFM_HOST
          parameters.append(
            "api_key", BuildConfig.API_KEY
          )
        }
      }
    }
  }
}
