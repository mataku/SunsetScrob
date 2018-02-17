package com.mataku.scrobscrob.app.model.entity

import com.google.gson.annotations.SerializedName

// uses getTopAlbums API

data class Album(
        @SerializedName("name")
        val name: String,

        @SerializedName("playcount")
        val playCount: String,

        @SerializedName("url")
        val url: String,

        @SerializedName("artist")
        val artist: Artist,

        @SerializedName("image")
        val imageList: List<Image>
)