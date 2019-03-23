package com.mataku.scrobscrob.core.entity

import com.squareup.moshi.Json

// for getTopAlbums, getTopArtists, ignore mbid param
// see: https://www.last.fm/api/show/user.getTopAlbums
data class Artist(
    @Json(name = "name")
    val name: String,

    @Json(name = "url")
    val url: String,

    @Json(name = "image")
    val image: List<Image>?,

    @Json(name = "playcount")
    val playcount: String?
)