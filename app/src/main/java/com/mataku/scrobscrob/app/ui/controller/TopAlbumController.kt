package com.mataku.scrobscrob.app.ui.controller

import com.airbnb.epoxy.TypedEpoxyController
import com.mataku.scrobscrob.ModelTopAlbumViewBindingModel_
import com.mataku.scrobscrob.app.model.entity.Album

class TopAlbumController : TypedEpoxyController<List<Album>>() {

    override fun buildModels(albums: List<Album>) {
        albums.forEachIndexed { index, album ->
            ModelTopAlbumViewBindingModel_()
                    .id(index)
                    .album(album)
                    .spanSizeOverride({ _, _, _ -> 1})
                    .addTo(this)
        }
    }
}