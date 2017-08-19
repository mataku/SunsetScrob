package com.mataku.scrobscrob.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.mataku.scrobscrob.app.model.Track
import com.mataku.scrobscrob.app.presenter.AppleMusicNotificationReceiverPresenter
import com.mataku.scrobscrob.app.ui.view.NotificationInterface
import com.mataku.scrobscrob.app.util.Settings

class AppleMusicNotificationReceiver : BroadcastReceiver(), NotificationInterface {
    var track: Track? = null
    var appSettings = Settings()

    override fun onReceive(context: Context, intent: Intent) {
        val presenter = AppleMusicNotificationReceiverPresenter(this)
        val sharedPreferences = context.getSharedPreferences("DATA", Context.MODE_PRIVATE)
        val sessionKey = sharedPreferences.getString("SessionKey", "")

        if (!(!sessionKey.isEmpty() && intent.action == "AppleMusic")) {
            Log.i("Notification", "Not Apple music notification")
            return
        }

        presenter.setNowPlayingIfDifferentTrack(track, intent, sessionKey)
    }

    override fun updateCurrentTrack(intent: Intent) {
        val artistName = intent.getStringExtra("artistName")
        val trackName = intent.getStringExtra("trackName")
        val albumName = intent.getStringExtra("albumName")
        val playingTime = intent.getLongExtra("playingTime", appSettings.defaultPlayingTime)
        val timeStamp = intent.getLongExtra("timeStamp", System.currentTimeMillis() / 1000L)

        track = Track(artistName, trackName, albumName, playingTime, timeStamp)
    }
}