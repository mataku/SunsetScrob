package com.mataku.scrobscrob.core.entity

import com.squareup.moshi.Json

// uses getTopAlbums API
data class Album(
    @Json(name = "name")
    val name: String,

    @Json(name = "playcount")
    val playCount: String,

    @Json(name = "url")
    val url: String,

    @Json(name = "artist")
    val artist: Artist,

    @Json(name = "image")
    val imageList: List<Image>
)