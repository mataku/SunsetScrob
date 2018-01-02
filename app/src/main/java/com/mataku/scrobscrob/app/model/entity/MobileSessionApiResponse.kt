package com.mataku.scrobscrob.app.model.entity

import com.google.gson.annotations.SerializedName

class MobileSessionApiResponse(@SerializedName("session")
                               val mobileSession: MobileSession) {

    data class MobileSession(
            val subscriber: Int,
            val name: String,
            val key: String
    )
}