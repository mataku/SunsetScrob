package com.mataku.scrobscrob.core.entity

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class AlbumInfo(
    @Json(name = "image")
    val imageList: List<Image>
)