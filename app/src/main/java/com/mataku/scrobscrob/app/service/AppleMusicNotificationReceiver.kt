package com.mataku.scrobscrob.app.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mataku.scrobscrob.app.model.Track
import com.mataku.scrobscrob.app.presenter.AppleMusicNotificationReceiverPresenter

class AppleMusicNotificationReceiver : BroadcastReceiver() {
    var track: Track? = null

    override fun onReceive(context: Context, intent: Intent) {
        val presenter = AppleMusicNotificationReceiverPresenter()
        val sharedPreferences = context.getSharedPreferences("DATA", Context.MODE_PRIVATE)
        val sessionKey = sharedPreferences.getString("SessionKey", "")

        if (sessionKey.isEmpty()) {
            return
        }

        presenter.setNowPlayingIfDifferentTrack(track, intent, sessionKey)
    }
}