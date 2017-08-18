package com.mataku.scrobscrob.app.model.entity

import com.google.gson.annotations.SerializedName

data class ScrobblesApiResponse(
        @SerializedName("scrobbles")
        val scrobbles: Scrobbles
)