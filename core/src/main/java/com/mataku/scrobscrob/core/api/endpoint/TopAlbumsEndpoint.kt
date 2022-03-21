package com.mataku.scrobscrob.core.api.endpoint

import io.ktor.http.HttpMethod
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class TopAlbumsEndpoint(
    override val path: String = "/2.0/?method=user.getTopAlbums&format=json",
    override val requestType: HttpMethod = HttpMethod.Get,
    override val params: Map<String, Any>
) : Endpoint

@Serializable
@SerialName("album")
data class Album(
    @SerialName("name")
    val name: String,
    @SerialName("url")
    val url: String,
    @SerialName("artist")
    val artist: AlbumArtist,
    @SerialName("image")
    val imageList: List<Image>?,
    @SerialName("playcount")
    val playcount: String
)

@Serializable
data class TopAlbumsApiResponse(
    @SerialName("topalbums")
    val topAlbums: TopAlbums
)

@Serializable
data class TopAlbums(
    @SerialName("album")
    val albums: List<Album>
)

@Serializable
@SerialName("artist")
data class AlbumArtist(
    @SerialName("name")
    val name: String
)