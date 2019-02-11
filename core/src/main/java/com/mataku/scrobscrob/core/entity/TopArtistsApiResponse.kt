package com.mataku.scrobscrob.core.entity

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
class TopArtistsApiResponse(@Json(name = "topartists") val topArtists: TopArtists) {

    @JsonSerializable
    data class TopArtists(
        @Json(name = "artist")
        val artists: List<Artist>
    )
}