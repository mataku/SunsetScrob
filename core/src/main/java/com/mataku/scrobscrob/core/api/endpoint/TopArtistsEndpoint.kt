package com.mataku.scrobscrob.core.api.endpoint

import io.ktor.http.HttpMethod
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class TopArtistsEndpoint(
    override val path: String = "/2.0/?method=user.getTopArtists&format=json",
    override val requestType: HttpMethod = HttpMethod.Get,
    override val params: Map<String, Any>
) : Endpoint

@Serializable
@SerialName("artist")
data class Artist(
    @SerialName("name")
    val name: String,
    @SerialName("url")
    val url: String,
    @SerialName("playcount")
    val playcount: String,
    @SerialName("image")
    val imageList: List<Image>?
)

@Serializable
data class TopArtistsApiResponse(
    @SerialName("topartists")
    val topArtists: TopArtists
)

@Serializable
data class TopArtists(
    @SerialName("artist")
    val artists: List<Artist>
)

@Serializable
data class Image(
    @SerialName("#text")
    val imageUrl: String
)