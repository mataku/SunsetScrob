package com.mataku.scrobscrob.app.model.entity

import com.google.gson.annotations.SerializedName

data class NowPlayingArtist(
        var corrected: String,

        @SerializedName("#text")
        var text: String
)