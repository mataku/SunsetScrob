package com.mataku.scrobscrob.app.model.entity

import com.google.gson.annotations.SerializedName

data class Artist(
        val corrected: String,

        @SerializedName("#text")
        val text: String
)