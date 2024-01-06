package com.mataku.scrobscrob.data.api

import com.mataku.scrobscrob.data.api.endpoint.Endpoint
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LastFmService @Inject constructor(val httpClient: HttpClient) {

  suspend inline fun <reified T> request(endpoint: Endpoint<T>): T {
    return when (val requestType = endpoint.requestType) {
      HttpMethod.Get -> {
        get(endpoint)
      }

      HttpMethod.Post -> {
        post(endpoint)
      }

      HttpMethod.Put -> {
        put(endpoint)
      }

      else -> {
        throw IllegalStateException("No handleable method: $requestType")
      }
    }
  }

  suspend inline fun <reified T> get(endpoint: Endpoint<T>): T {
    val response = httpClient.get {
      url(endpoint.path)
      if (endpoint.params.isNotEmpty()) {
        endpoint.params.forEach { (k, v) ->
          parameter(k, v)
        }
      }
    }
    return response.body()
  }

  suspend inline fun <reified T> post(endpoint: Endpoint<T>): T {
    val response = httpClient.post {
      url(endpoint.path)
      setBody("")
      endpoint.params.forEach { (k, v) ->
        parameter(k, v)
      }
    }

    return response.body()
  }

  suspend inline fun <reified T> put(endpoint: Endpoint<T>): T {
    val response = httpClient.put {
      url(endpoint.path)
      setBody("")
      endpoint.params.forEach { (k, v) ->
        parameter(k, v)
      }
    }

    return response.body()
  }
}
