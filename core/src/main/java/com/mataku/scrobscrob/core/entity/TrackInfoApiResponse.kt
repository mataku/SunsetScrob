package com.mataku.scrobscrob.core.entity

import com.squareup.moshi.Json

class TrackInfoApiResponse(
    @Json(name = "track")
    val trackInfo: TrackInfo?
) {

    data class TrackInfo(
        @Json(name = "duration")
        val duration: String?,

        @Json(name = "album")
        val album: AlbumInfo?
    )
}