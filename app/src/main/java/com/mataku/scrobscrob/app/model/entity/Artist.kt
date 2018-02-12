package com.mataku.scrobscrob.app.model.entity

import com.google.gson.annotations.SerializedName

// for getTopAlbums, ignore mbid param
// see: https://www.last.fm/api/show/user.getTopAlbums

data class Artist(
        @SerializedName("name")
        val name: String,

        @SerializedName("url")
        val url: String
)