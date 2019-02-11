package com.mataku.scrobscrob.core.entity

import com.squareup.moshi.Json

data class AlbumInfo(
    @Json(name = "image")
    val imageList: List<Image>
)