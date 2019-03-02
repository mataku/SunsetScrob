package com.mataku.scrobscrob.core.entity

import com.squareup.moshi.Json

class TopArtistsApiResponse(@Json(name = "topartists") val topArtists: TopArtists) {

    data class TopArtists(
        @Json(name = "artist")
        val artists: List<Artist>
    )
}