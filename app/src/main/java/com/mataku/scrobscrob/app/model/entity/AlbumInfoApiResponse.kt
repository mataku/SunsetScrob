package com.mataku.scrobscrob.app.model.entity

import com.google.gson.annotations.SerializedName

data class AlbumInfoApiResponse(
        @SerializedName("album")
        val albumInfo: AlbumInfo
)