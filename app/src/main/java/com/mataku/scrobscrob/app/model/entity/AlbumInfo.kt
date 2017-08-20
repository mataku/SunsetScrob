package com.mataku.scrobscrob.app.model.entity

import com.google.gson.annotations.SerializedName

data class AlbumInfo(
        @SerializedName("image")
        val imageList: List<Image>
)