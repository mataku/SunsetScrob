package com.mataku.scrobscrob.core.entity

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class ScrobblesApiResponse(
    @Json(name = "scrobbles")
    val scrobbles: Scrobbles
)