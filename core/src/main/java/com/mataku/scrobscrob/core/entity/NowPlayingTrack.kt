package com.mataku.scrobscrob.core.entity

import com.squareup.moshi.Json

data class NowPlayingTrack(
    val corrected: String,

    @Json(name = "#text")
    val text: String
)