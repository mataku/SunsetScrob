package com.mataku.scrobscrob.app.model.entity

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

// uses getTopAlbums API
@JsonSerializable
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