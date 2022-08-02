package com.mataku.scrobscrob.data.api

import com.mataku.scrobscrob.core.api.endpoint.Endpoint
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LastFmService @Inject constructor(val httpClient: HttpClient) {

  suspend inline fun <reified T> get(endpoint: Endpoint): T {
    val response = httpClient.get {
      url(BASE_URL + endpoint.path)
      if (endpoint.params.isNotEmpty()) {
        endpoint.params.forEach { (k, v) ->
          parameter(k, v)
        }
      }
    }

    return response.body()
  }

  suspend inline fun <reified T> post(endpoint: Endpoint): T {
    val response = httpClient.post {
      url(BASE_URL + endpoint.path)
      setBody("")
      endpoint.params.forEach { (k, v) ->
        parameter(k, v)
      }
    }

    return response.body()
  }

  companion object {
    const val BASE_URL = "https://ws.audioscrobbler.com"
  }
}
