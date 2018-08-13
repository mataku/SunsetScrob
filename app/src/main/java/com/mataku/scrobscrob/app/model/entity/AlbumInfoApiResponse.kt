package com.mataku.scrobscrob.app.model.entity

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class AlbumInfoApiResponse(
        @Json(name = "album")
        val albumInfo: AlbumInfo
)