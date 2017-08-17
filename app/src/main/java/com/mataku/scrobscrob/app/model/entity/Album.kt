package com.mataku.scrobscrob.app.model.entity

import com.google.gson.annotations.SerializedName

data class Album(
        val corrected: String,

        @SerializedName("#text")
        val text: String
)