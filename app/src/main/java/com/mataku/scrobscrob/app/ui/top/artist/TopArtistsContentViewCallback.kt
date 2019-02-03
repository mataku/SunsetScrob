package com.mataku.scrobscrob.app.ui.top.artist

import com.mataku.scrobscrob.app.model.entity.Artist

interface TopArtistsContentViewCallback {
    fun show(artists: List<Artist>)
}