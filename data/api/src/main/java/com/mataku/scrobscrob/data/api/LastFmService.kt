package com.mataku.scrobscrob.data.api

import com.mataku.scrobscrob.data.api.endpoint.Endpoint
import com.mataku.scrobscrob.data.api.endpoint.HttpMethod
import com.mataku.scrobscrob.data.api.endpoint.LastFmApiError
import com.mataku.scrobscrob.data.api.endpoint.LastFmErrorResponse
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.URLProtocol
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.typeInfo
import kotlinx.serialization.json.Json

interface LastFmService {
  suspend fun rawRequest(endpoint: Endpoint<*>, typeInfo: TypeInfo): Any
}

@SingleIn(AppScope::class)
@Inject
internal class LastFmServiceImpl(engine: HttpClientEngine) : LastFmService {

  private val httpClient: HttpClient = HttpClient(engine) {
    install(ContentNegotiation) {
      json(
        Json {
          isLenient = true
          ignoreUnknownKeys = true
        },
      )
    }
    defaultRequest {
      url {
        protocol = URLProtocol.HTTPS
        host = LASTFM_HOST
      }
    }
  }

  override suspend fun rawRequest(endpoint: Endpoint<*>, typeInfo: TypeInfo): Any =
    when (endpoint.requestType) {
      HttpMethod.GET -> doGet(endpoint, typeInfo)
      HttpMethod.POST -> doPost(endpoint, typeInfo)
      HttpMethod.PUT -> doPut(endpoint, typeInfo)
    }

  private suspend fun doGet(endpoint: Endpoint<*>, typeInfo: TypeInfo): Any {
    val response = httpClient.get {
      url(endpoint.path)
      endpoint.params.forEach { (k, v) -> parameter(k, v) }
    }
    return response.body(typeInfo)
  }

  private suspend fun doPost(endpoint: Endpoint<*>, typeInfo: TypeInfo): Any {
    val response = httpClient.post {
      url(endpoint.path)
      setBody("")
      endpoint.params.forEach { (k, v) -> parameter(k, v) }
    }
    if (response.status.isSuccess()) {
      return response.body(typeInfo)
    }
    val errorMessage = runCatching {
      response.body<LastFmErrorResponse>().message
    }.getOrNull() ?: "Unknown error"
    throw LastFmApiError(errorMessage)
  }

  private suspend fun doPut(endpoint: Endpoint<*>, typeInfo: TypeInfo): Any {
    val response = httpClient.put {
      url(endpoint.path)
      setBody("")
      endpoint.params.forEach { (k, v) -> parameter(k, v) }
    }
    return response.body(typeInfo)
  }

  private companion object {
    const val LASTFM_HOST = "ws.audioscrobbler.com"
  }
}

@Suppress("UNCHECKED_CAST")
suspend inline fun <reified T : Any> LastFmService.request(endpoint: Endpoint<T>): T =
  rawRequest(endpoint, typeInfo<T>()) as T
