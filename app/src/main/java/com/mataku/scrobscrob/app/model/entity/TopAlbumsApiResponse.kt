package com.mataku.scrobscrob.app.model.entity

import com.google.gson.annotations.SerializedName

class TopAlbumsApiResponse(@SerializedName("topalbums") val topAlbums: TopAlbums) {

    data class TopAlbums(
            @SerializedName("album")
            val albums: List<Album>
    )
}