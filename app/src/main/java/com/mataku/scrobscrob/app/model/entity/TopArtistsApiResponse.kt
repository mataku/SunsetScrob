package com.mataku.scrobscrob.app.model.entity

import com.google.gson.annotations.SerializedName

class TopArtistsApiResponse(@SerializedName("topartists") val topArtists: TopArtists) {

    data class TopArtists(
            @SerializedName("artist")
            val artists: List<Artist>
    )
}