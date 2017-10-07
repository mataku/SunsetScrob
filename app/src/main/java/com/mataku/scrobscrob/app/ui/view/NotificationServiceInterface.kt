package com.mataku.scrobscrob.app.ui.view

import com.mataku.scrobscrob.app.model.Track

interface NotificationServiceInterface {
    fun setAlbumArtwork(albumArtWork: String)
    fun saveScrobble(track: Track)
    fun setPlayingTime(playingTime: Long)
}