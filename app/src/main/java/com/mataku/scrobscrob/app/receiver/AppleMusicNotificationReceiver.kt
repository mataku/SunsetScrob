package com.mataku.scrobscrob.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AppleMusicNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != "AppleMusic") {
            return
        }
    }
}