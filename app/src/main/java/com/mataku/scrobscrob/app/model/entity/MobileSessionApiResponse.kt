package com.mataku.scrobscrob.app.model.entity

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
class MobileSessionApiResponse(
    @Json(name = "session")
    val mobileSession: MobileSession
) {

    @JsonSerializable
    data class MobileSession(
        val subscriber: Int,
        val name: String,
        val key: String
    )
}