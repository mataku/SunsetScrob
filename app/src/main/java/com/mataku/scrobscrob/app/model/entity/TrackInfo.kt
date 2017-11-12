package com.mataku.scrobscrob.app.model.entity

import com.google.gson.annotations.SerializedName

data class TrackInfo(
        @SerializedName("duration")
        val duration: String,

        @SerializedName("album")
        val album: AlbumInfo
)