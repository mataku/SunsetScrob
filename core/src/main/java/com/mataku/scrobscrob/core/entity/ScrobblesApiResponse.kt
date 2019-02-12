package com.mataku.scrobscrob.core.entity

import com.squareup.moshi.Json

data class ScrobblesApiResponse(
    @Json(name = "scrobbles")
    val scrobbles: Scrobbles
)