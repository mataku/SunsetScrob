package com.mataku.scrobscrob.core.entity

import com.squareup.moshi.Json

data class NowPlayingArtist(
    var corrected: String,

    @Json(name = "#text")
    var text: String
)