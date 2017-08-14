package com.mataku.scrobscrob.app.service

import android.content.Intent
import android.content.pm.PackageManager
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.mataku.scrobscrob.app.model.Track


class AppleMusicNotificationService : NotificationListenerService() {
    private val APPLE_MUSIC_PACKAGE_NAME = "com.apple.android.music"

    override fun onCreate() {
        super.onCreate()
        try {
            val appleMusicPackageInfo = packageManager.getPackageInfo(APPLE_MUSIC_PACKAGE_NAME, 0)
            Log.i("AppleMusicNotification", "Apple music is installed! (version: ${appleMusicPackageInfo.versionCode}")
        } catch (e: PackageManager.NameNotFoundException) {
            Log.i("AppleMusicNotification", "Apple music is NOT installed!")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    // ステータスバーに通知が更新される
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)

        // Ignore if not Apple Music APP
        if (sbn.packageName != APPLE_MUSIC_PACKAGE_NAME) {
            Log.i("Notification", "Not apple music notification")
            return
        }

        val extra = sbn.notification.extras

        // song title
        // Format: android.title=songName
        val name = extra.get("android.title").toString()

        // songName and artistName
        // Format: android.text=artistName - albumName
        // e.g. PassCode — VIRTUAL
        val albumInfo = extra.get("android.text")
        albumInfo ?: return

        val array = albumInfo.toString().split("-")
        val artistName = array[0].trim()
        val albumName = array[1].trim()
        val track = Track(artistName, name, albumName)

        var intent: Intent = Intent("AppleMusic")
        intent.putExtra("message", "Yes!!!")
        sendBroadcast(intent)
    }

    // ステータスバーから通知が消去される
    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        super.onNotificationRemoved(sbn)

        var intent: Intent = Intent("AppleMusic")
        intent.putExtra("message", "Yes!!!")
        sendBroadcast(intent)
    }
}