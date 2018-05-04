package com.mataku.scrobscrob.app.model.entity

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class Image(
        @Json(name = "#text")
        val imageUrl: String,

        @Json(name = "size")
        val size: String
)