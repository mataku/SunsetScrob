package com.mataku.scrobscrob.core.entity

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class AlbumInfoApiResponse(
    @Json(name = "album")
    val albumInfo: AlbumInfo
)