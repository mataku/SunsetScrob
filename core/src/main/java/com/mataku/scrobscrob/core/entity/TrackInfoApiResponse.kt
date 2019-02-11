package com.mataku.scrobscrob.core.entity

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
class TrackInfoApiResponse(
    @Json(name = "track")
    val trackInfo: TrackInfo?
) {

    @JsonSerializable
    data class TrackInfo(
        @Json(name = "duration")
        val duration: String?,

        @Json(name = "album")
        val album: AlbumInfo?
    )
}