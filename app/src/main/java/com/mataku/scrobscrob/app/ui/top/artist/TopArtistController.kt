package com.mataku.scrobscrob.app.ui.top.artist

import android.content.Context
import com.airbnb.epoxy.EpoxyController
import com.mataku.scrobscrob.app.model.entity.Artist
import com.mataku.scrobscrob.app.ui.widget.TopArtistViewModel_

class TopArtistController(val context: Context?) : EpoxyController() {

    private var artists: MutableList<Artist> = mutableListOf()

    override fun buildModels() {
        context?.let {
            val displayMetrics = it.resources.displayMetrics
//        val density = displayMetrics.density
//        val leftSpace = 16 * density
            val halfWidth = displayMetrics.widthPixels / 2
            artists.forEachIndexed { index, artist ->
                TopArtistViewModel_()
                    .id("artist$index")
                    .artist(artist)
                    .spanSizeOverride { _, _, _ -> 1 }
                    .imageSize(halfWidth)
                    .addTo(this)
            }
        }
    }

    fun setArtists(artists: List<Artist>) {
        this.artists.clear()
        this.artists.addAll(artists)
        requestModelBuild()
    }
}