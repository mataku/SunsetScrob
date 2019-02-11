package com.mataku.scrobscrob.core.entity

import com.squareup.moshi.Json

data class AlbumInfoApiResponse(
    @Json(name = "album")
    val albumInfo: AlbumInfo
)