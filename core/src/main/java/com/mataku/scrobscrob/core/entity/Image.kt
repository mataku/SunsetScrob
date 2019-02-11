package com.mataku.scrobscrob.core.entity

import com.squareup.moshi.Json

data class Image(
    @Json(name = "#text")
    val imageUrl: String,

    @Json(name = "size")
    val size: String
)