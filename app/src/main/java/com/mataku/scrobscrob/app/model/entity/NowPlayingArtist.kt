package com.mataku.scrobscrob.app.model.entity

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class NowPlayingArtist(
    var corrected: String,

    @Json(name = "#text")
    var text: String
)