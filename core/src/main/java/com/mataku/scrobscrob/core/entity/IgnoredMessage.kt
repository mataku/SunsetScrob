package com.mataku.scrobscrob.core.entity

import com.squareup.moshi.Json

data class IgnoredMessage(
    val code: String,

    @Json(name = "#text")
    val text: String
)