package com.mataku.scrobscrob.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mataku.scrobscrob.app.model.Track
import com.mataku.scrobscrob.app.presenter.AppleMusicNotificationReceiverPresenter
import com.mataku.scrobscrob.app.ui.view.NotificationInterface

class AppleMusicNotificationReceiver : BroadcastReceiver(), NotificationInterface {
    var track: Track? = null

    override fun onReceive(context: Context, intent: Intent) {
        val presenter = AppleMusicNotificationReceiverPresenter(this)
        val sharedPreferences = context.getSharedPreferences("DATA", Context.MODE_PRIVATE)
        val sessionKey = sharedPreferences.getString("SessionKey", "")

        if (sessionKey.isEmpty()) {
            return
        }

        presenter.setNowPlayingIfDifferentTrack(track, intent, sessionKey)
    }

    override fun updateCurrentTrack(intent: Intent) {
        val artistName = intent.getStringExtra("artistName")
        val trackName = intent.getStringExtra("trackName")
        val albumName = intent.getStringExtra("albumName")
        // TODO: track.getInfo
        val playingTime = intent.getLongExtra("playingTime", 0.toLong())
        track = Track(artistName, trackName, albumName, playingTime)
    }
}