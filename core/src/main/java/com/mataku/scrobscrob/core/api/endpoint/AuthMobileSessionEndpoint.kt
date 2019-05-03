package com.mataku.scrobscrob.core.api.endpoint

import io.ktor.http.HttpMethod
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class AuthMobileSessionEndpoint(
    override val path: String = "/2.0/?method=auth.getMobileSession&format=json",
    override val requestType: HttpMethod = HttpMethod.Post,
    override val params: Map<String, String>
) : Endpoint

@Serializable
data class AuthMobileSessionApiResponse(
    @SerialName("session")
    val mobileSession: MobileSession?
)

@Serializable
data class MobileSession(
    @SerialName("name")
    val name: String,
    @SerialName("key")
    val key: String
)