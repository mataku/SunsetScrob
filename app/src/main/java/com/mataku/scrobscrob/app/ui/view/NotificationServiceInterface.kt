package com.mataku.scrobscrob.app.ui.view

import com.mataku.scrobscrob.app.model.Track

interface NotificationServiceInterface {
    fun setAlbumArtwork(albumArtWork: String)
    fun saveScrobble(track: Track)
    fun setCurrentTrackInfo(playingTime: Long, albumArtWork: String)
    fun notifyNowPlayingUpdated(track: Track)
}