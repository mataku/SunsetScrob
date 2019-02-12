package com.mataku.scrobscrob.core.entity

import com.squareup.moshi.Json

class MobileSessionApiResponse(
    @Json(name = "session")
    val mobileSession: MobileSession
) {

    data class MobileSession(
        val subscriber: Int,
        val name: String,
        val key: String
    )
}