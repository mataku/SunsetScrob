package com.mataku.scrobscrob.core.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mataku.scrobscrob.core.BuildConfig
import com.mataku.scrobscrob.core.api.endpoint.Endpoint
import com.mataku.scrobscrob.core.api.okhttp.LastfmApiAuthInterceptor
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.logging.SIMPLE
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import kotlinx.serialization.json.Json

object ApiClient {
    const val BASE_URL = "https://ws.audioscrobbler.com"

    val client: HttpClient
        get() {
            return HttpClient(OkHttp) {
                engine {
                    addInterceptor(LastfmApiAuthInterceptor())
                    response.apply {
                        defaultCharset = Charsets.UTF_8
                    }
                }
                install(JsonFeature) {
                    serializer = KotlinxSerializer(Json.nonstrict)
                }

                if (BuildConfig.DEBUG) {
                    install(Logging) {
                        logger = Logger.SIMPLE
                        level = LogLevel.ALL
                    }
                }
            }
        }

    suspend inline fun <reified T> request(endpoint: Endpoint): T {
        val response = client.request<T>(BASE_URL + endpoint.path) {
            method = endpoint.requestType
            if (endpoint.params.isNotEmpty()) {
                endpoint.params.forEach { (k, v) ->
                    parameter(key = k, value = v)
                }
            }
        }
        client.close()
        return response
    }

    // charset=utf-8 になってない気がする
    suspend inline fun <reified T> get(endpoint: Endpoint): T {
        val response = client.get<T>(BASE_URL + endpoint.path) {
            if (endpoint.params.isNotEmpty()) {
                endpoint.params.forEach { (k, v) ->
                    parameter(key = k, value = v)
                }
            }
        }
        client.close()
        return response
    }

    // charset=utf-8 になってない気がする
    suspend inline fun <reified T> req(endpoint: Endpoint): LiveData<T> {
        val liveData = MutableLiveData<T>()
        val response = client.get<T>(BASE_URL + endpoint.path) {
            if (endpoint.params.isNotEmpty()) {
                endpoint.params.forEach { (k, v) ->
                    parameter(key = k, value = v)
                }
            }
        }
        client.close()
        return liveData
    }

}