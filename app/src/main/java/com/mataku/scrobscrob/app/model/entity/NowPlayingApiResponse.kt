package com.mataku.scrobscrob.app.model.entity

import com.google.gson.annotations.SerializedName

data class NowPlayingApiResponse(
        @SerializedName("nowplaying")
        val nowPlaying: NowPlaying
)