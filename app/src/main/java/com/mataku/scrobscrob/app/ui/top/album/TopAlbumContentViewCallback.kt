package com.mataku.scrobscrob.app.ui.top.album

import com.mataku.scrobscrob.app.model.entity.Album

interface TopAlbumContentViewCallback {
    fun show(albums: List<Album>)
}