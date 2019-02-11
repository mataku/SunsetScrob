package com.mataku.scrobscrob.core.entity

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
class TopAlbumsApiResponse(@Json(name = "topalbums") val topAlbums: TopAlbums) {

    @JsonSerializable
    data class TopAlbums(
        @Json(name = "album")
        val albums: List<Album>
    )
}