package com.mataku.scrobscrob.app.ui.controller

import com.airbnb.epoxy.EpoxyController
import com.mataku.scrobscrob.app.model.entity.Artist
import com.mataku.scrobscrob.app.ui.widget.UserTopArtistViewModel_

class TopArtistController : EpoxyController() {

    private var artists: MutableList<Artist> = mutableListOf()

    override fun buildModels() {
        artists.forEachIndexed { index, artist ->
            UserTopArtistViewModel_()
                    .id("artist$index")
                    .artist(artist)
                    .spanSizeOverride({ _, _, _ -> 1 })
                    .onBind { _, view, _ ->
                        run {
                            val displayMetrics = view.context.resources.displayMetrics
                            val density = displayMetrics.density
                            val leftSpace = 16 * density
//                            val spanCount = spanSizeList.get(index % 10)
                            val halfWidth = displayMetrics.widthPixels / 2 - leftSpace
                            view.setImageSize(halfWidth.toInt())
                        }
                    }
                    .addTo(this)
        }

    }

    fun setArtists(artists: List<Artist>) {
        this.artists.clear()
        this.artists.addAll(artists)
        requestModelBuild()
    }
}