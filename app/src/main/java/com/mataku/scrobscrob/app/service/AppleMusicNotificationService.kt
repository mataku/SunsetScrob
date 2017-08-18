package com.mataku.scrobscrob.app.service

import android.content.Intent
import android.content.pm.PackageManager
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.mataku.scrobscrob.app.model.Track
import com.mataku.scrobscrob.app.presenter.AppleMusicNotificationServicePresenter
import com.mataku.scrobscrob.app.ui.view.NotificationServiceInterface


class AppleMusicNotificationService : NotificationListenerService(), NotificationServiceInterface {
    private val APPLE_MUSIC_PACKAGE_NAME = "com.apple.android.music"
    private val presenter = AppleMusicNotificationServicePresenter(this)

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

        // track name
        // Format: android.title=songName
        val extraTitle = extra.get("android.title")
        extraTitle ?: return
        val trackName = extraTitle.toString()

        // artist name and album name
        // Format: android.text=artistName - albumName
        // e.g. PassCode — VIRTUAL
        val albumInfo = extra.get("android.text")
        albumInfo ?: return

        val array = albumInfo.toString().split("—".toRegex(), 2).dropLastWhile { it.isEmpty() }.toTypedArray()
        val artistName = array[0].trim()
        val albumName = array[1].trim()
        val playingTime = 300000.toLong()
        val track = Track(
                artistName,
                trackName,
                albumName,
                playingTime
        )
        presenter.getTrackInfo(track)
    }

    // ステータスバーから通知が消去される
    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        super.onNotificationRemoved(sbn)

        val intent = Intent("AppleMusic")
        sendBroadcast(intent)
    }

    override fun sendTrackInfoToReceiver(track: Track) {
        val intent = Intent("AppleMusic")
        intent.putExtra("artistName", track.artistName)
        intent.putExtra("trackName", track.name)
        intent.putExtra("albumName", track.albumName)
        intent.putExtra("playingTime", track.playingTime)
        sendBroadcast(intent)
    }
}