package com.mataku.scrobscrob.core.api.endpoint

import io.ktor.http.HttpMethod

interface Endpoint {
    val path: String
    val params: Map<String, Any>
        get() = mapOf()
    val requestType: HttpMethod
}