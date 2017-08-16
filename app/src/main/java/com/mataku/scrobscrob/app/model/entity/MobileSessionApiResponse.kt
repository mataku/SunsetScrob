package com.mataku.scrobscrob.app.model.entity

import com.google.gson.annotations.SerializedName

data class MobileSessionApiResponse(
        @SerializedName("session")
        val mobileSession: MobileSession
)