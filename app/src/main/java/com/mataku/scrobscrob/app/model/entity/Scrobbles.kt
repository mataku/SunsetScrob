package com.mataku.scrobscrob.app.model.entity

import com.google.gson.annotations.SerializedName

data class Scrobbles(
        @SerializedName("@attr")
        val attr: ScrobbleAttr,
        @SerializedName("scrobble")
        val scrobble: Scrobble
)