package com.mataku.scrobscrob.app.model.entity

import com.google.gson.annotations.SerializedName

data class AlbumArtist(
        var corrected: String,

        @SerializedName("#text")
        var text: String
)