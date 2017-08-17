package com.mataku.scrobscrob.app.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.mataku.scrobscrob.app.model.Track
import com.mataku.scrobscrob.app.presenter.AppleMusicNotificationReceiverPresenter

class AppleMusicNotificationReceiver : BroadcastReceiver() {
    var previousTrackName: String = ""
    var previousAlbumName: String = ""
    var previousArtistName: String = ""
    var previousTrackStartedAt: String = ""
    var track: Track? = null

    override fun onReceive(context: Context, intent: Intent) {
        val presenter = AppleMusicNotificationReceiverPresenter()
        val artistName = intent.getStringExtra("artistName")
        val trackName = intent.getStringExtra("trackName")
        val albumName = intent.getStringExtra("albumName")
        val playingTime = intent.getLongExtra("playingTime", 0)
        val sharedPreferences = context.getSharedPreferences("DATA", Context.MODE_PRIVATE)
        val sessionKey = sharedPreferences.getString("SessionKey", "")
        println(sessionKey)
        if (track == null) {
            track = Track(artistName, trackName, albumName, playingTime)
        } else {
            if (sessionKey.isNotEmpty()) {
                presenter.setNowPlaying(track!!, sessionKey)
            } else {
                Log.i("Notification", "No session key")
            }
        }
    }
}