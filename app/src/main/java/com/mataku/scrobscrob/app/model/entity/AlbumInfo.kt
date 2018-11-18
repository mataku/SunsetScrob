package com.mataku.scrobscrob.app.model.entity

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class AlbumInfo(
    @Json(name = "image")
    val imageList: List<Image>
)