package com.mataku.scrobscrob.app.model.entity

import com.google.gson.annotations.SerializedName

data class Image(
        @SerializedName("#text")
        val imageUrl: String,

        @SerializedName("size")
        val size: String
)