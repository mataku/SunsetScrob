package com.mataku.scrobscrob.core.api

import com.mataku.scrobscrob.core.BuildConfig
import com.mataku.scrobscrob.core.api.endpoint.Endpoint
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.android.Android
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.logging.SIMPLE
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import kotlinx.serialization.json.Json

object ApiClient {
    const val BASE_URL = "https://ws.audioscrobbler.com"

    val client: HttpClient
        get() {
            val config: HttpClientConfig<*>.() -> Unit = {
                install(JsonFeature) {
                    serializer = KotlinxSerializer(Json.nonstrict)
                }
                defaultRequest {
                    parameter("api_key", BuildConfig.API_KEY)
                }
                if (BuildConfig.DEBUG) {
                    install(Logging) {
                        logger = Logger.SIMPLE
                        level = LogLevel.ALL
                    }
                }
            }
            return HttpClient(Android).config(config)
        }

    suspend inline fun <reified T> request(endpoint: Endpoint): T {
        val response = client.request<T>(BASE_URL + endpoint.path) {
            method = endpoint.requestType
            if (endpoint.params.isNotEmpty()) {
                endpoint.params.forEach { k, v ->
                    parameter(key = k, value = v)
                }
            }
        }
        client.close()
        return response
    }
}