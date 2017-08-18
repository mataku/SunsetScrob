package com.mataku.scrobscrob.app.model.entity

import com.google.gson.annotations.SerializedName

data class TrackInfoApiResponse(
        @SerializedName("track")
        val trackInfo: TrackInfo
)