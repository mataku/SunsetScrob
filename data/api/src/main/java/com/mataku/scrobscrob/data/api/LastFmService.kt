package com.mataku.scrobscrob.data.api

import com.mataku.scrobscrob.core.api.endpoint.Endpoint
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LastFmService @Inject constructor(val httpClient: HttpClient) {

    suspend inline fun <reified T> get(endpoint: Endpoint): T {
        val response = httpClient.get<T>(BASE_URL + endpoint.path) {
            if (endpoint.params.isNotEmpty()) {
                endpoint.params.forEach { (k, v) ->
                    parameter(key = k, value = v)
                }
            }
        }
        return response
    }

    suspend inline fun <reified T> post(endpoint: Endpoint): T {
        val response = httpClient.post<T>(BASE_URL + endpoint.path) {
            // Fix at ktor 1.2
            // https://github.com/ktorio/ktor/issues/904
            body = ""
            endpoint.params.forEach { (k, v) ->
                parameter(k, v)
            }
        }

        return response
    }

    companion object {
        const val BASE_URL = "https://ws.audioscrobbler.com"
    }
}