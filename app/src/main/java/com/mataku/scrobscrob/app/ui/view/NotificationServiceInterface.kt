package com.mataku.scrobscrob.app.ui.view

import com.mataku.scrobscrob.app.model.Track

interface NotificationServiceInterface {
    fun saveScrobble(track: Track)
    fun setTrackPlayingTime(playingTime: Long)
    fun setAlbumArtwork(albumArtWork: String)
}