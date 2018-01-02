package com.mataku.scrobscrob.app.model.entity

import com.google.gson.annotations.SerializedName

class TrackInfoApiResponse(@SerializedName("track")
                           val trackInfo: TrackInfo) {

    data class TrackInfo(
            @SerializedName("duration")
            val duration: String,

            @SerializedName("album")
            val album: AlbumInfo
    )
}