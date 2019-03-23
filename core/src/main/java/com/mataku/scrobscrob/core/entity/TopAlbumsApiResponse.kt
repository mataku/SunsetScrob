package com.mataku.scrobscrob.core.entity

import com.squareup.moshi.Json

class TopAlbumsApiResponse(@Json(name = "topalbums") val topAlbums: TopAlbums) {

    data class TopAlbums(
        @Json(name = "album")
        val albums: List<Album>
    )
}