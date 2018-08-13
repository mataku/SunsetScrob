package com.mataku.scrobscrob.app.ui.controller

import android.content.Context
import com.airbnb.epoxy.EpoxyController
import com.mataku.scrobscrob.app.model.entity.Album
import com.mataku.scrobscrob.app.ui.widget.TopAlbumViewModel_

class TopAlbumController(val context: Context?) : EpoxyController() {

    private val albums: MutableList<Album> = mutableListOf()

    //    private val spanSizeList = listOf(6, 3, 3, 3, 3, 6, 4, 4, 4, 6, 3, 3, 3, 3, 6)

    private val spanSizeList = listOf(4, 4, 4, 4, 4, 4, 3, 3, 3, 3)

    override fun buildModels() {
        context?.let {
            val displayMetrics = it.resources.displayMetrics
            val density = displayMetrics.density
            val leftSpace = 16 * density
//                            val spanCount = spanSizeList.get(index % 10)
            val halfWidth = displayMetrics.widthPixels / 2 - leftSpace

            albums.forEachIndexed { index, album ->
                TopAlbumViewModel_()
                        .id(index)
                        .album(album)
                        .spanSizeOverride { _, _, _ -> 1 }
                        .imageSize(halfWidth.toInt())
                        .addTo(this)
            }

        }
    }

    fun setAlbums(albums: List<Album>) {
        this.albums.addAll(albums)
        requestModelBuild()
    }

    private fun widthChooser(index: Int): Int {
        val list = listOf(2, 4, 4, 4, 4, 4, 4, 4, 4, 2, 3, 3, 3, 2, 2)
        return list.get(index % 15)
    }
}