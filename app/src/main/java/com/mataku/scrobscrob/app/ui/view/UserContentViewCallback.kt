package com.mataku.scrobscrob.app.ui.view

import com.mataku.scrobscrob.app.model.entity.Album

interface UserContentViewCallback {
    fun show(albums: List<Album>)
}