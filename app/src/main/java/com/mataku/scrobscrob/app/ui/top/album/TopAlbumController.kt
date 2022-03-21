package com.mataku.scrobscrob.app.ui.top.album

import com.airbnb.epoxy.EpoxyController
import com.mataku.scrobscrob.app.ui.widget.TopAlbumViewModel_
import com.mataku.scrobscrob.core.api.endpoint.Album

class TopAlbumController(private val halfWidth: Int) : EpoxyController() {

    private val albums: MutableList<Album> = mutableListOf()

    //    private val spanSizeList = listOf(6, 3, 3, 3, 3, 6, 4, 4, 4, 6, 3, 3, 3, 3, 6)

    private val spanSizeList = listOf(4, 4, 4, 4, 4, 4, 3, 3, 3, 3)

    override fun buildModels() {
        albums.distinct().forEachIndexed { index, album ->
            TopAlbumViewModel_()
                .id(index)
                .album(album)
                .spanSizeOverride { _, _, _ -> 1 }
                .imageSize(halfWidth)
                .addTo(this)
        }
    }

    fun setAlbums(albumList: List<Album>) {
        this.albums.addAll(albumList)
        requestModelBuild()
    }

    private fun widthChooser(index: Int): Int {
        val list = listOf(2, 4, 4, 4, 4, 4, 4, 4, 4, 2, 3, 3, 3, 2, 2)
        return list.get(index % 15)
    }
}