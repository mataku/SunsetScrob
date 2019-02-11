package com.mataku.scrobscrob.core.entity

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class NowPlayingAlbum(
    val corrected: String,

    @Json(name = "#text")
    val text: String
)