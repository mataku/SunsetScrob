package com.mataku.scrobscrob.app.ui.top.artist

import com.airbnb.epoxy.EpoxyController
import com.mataku.scrobscrob.app.ui.widget.TopArtistViewModel_
import com.mataku.scrobscrob.core.api.endpoint.Artist

class TopArtistController(private val halfWidth: Int) : EpoxyController() {

    private var artists: MutableList<Artist> = mutableListOf()

    override fun buildModels() {
        artists.distinct().forEachIndexed { index, artist ->
            TopArtistViewModel_()
                .id("artist$index")
                .artist(artist)
                .spanSizeOverride { _, _, _ -> 1 }
                .imageSize(halfWidth)
                .addTo(this)
        }
    }

    fun setArtists(artists: List<Artist>) {
        this.artists.addAll(artists)
        requestModelBuild()
    }
}