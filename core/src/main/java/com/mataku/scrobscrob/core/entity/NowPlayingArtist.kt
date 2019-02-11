package com.mataku.scrobscrob.core.entity

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class NowPlayingArtist(
    var corrected: String,

    @Json(name = "#text")
    var text: String
)