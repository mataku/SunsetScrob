package com.mataku.scrobscrob.app.service

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
    private var track: Track? = null

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
        val sharedPreferences = getSharedPreferences("DATA", Context.MODE_PRIVATE)
        val sessionKey = sharedPreferences.getString("SessionKey", "")
        val previousTrackName = sharedPreferences.getString("PreviousTrackName", "")

        // Ignore if not Apple Music APP
        if (sbn.packageName != APPLE_MUSIC_PACKAGE_NAME) {
            Log.i("Notification", "Not apple music notification")
            return
        }

        // Do not request when not logged in
        if (sessionKey.isEmpty()) {
            return
        }

        val extra = sbn.notification.extras

        // track name
        // Format: android.title=songName
        val extraTitle = extra.get("android.title")
        extraTitle ?: return
        val trackName = extraTitle.toString()

        if (previousTrackName == trackName) {
            return
        }

        setPreviousTrackName(trackName)
        // artist name and album name
        // Format: android.text=artistName - albumName
        // e.g. PassCode — VIRTUAL
        val albumInfo = extra.get("android.text")
        albumInfo ?: return

        val array = albumInfo.toString().split(" — ".toRegex(), 2).dropLastWhile { it.isEmpty() }.toTypedArray()
        val artistName = array[0].trim()
        val albumName = array[1].trim()
        val timeStamp = System.currentTimeMillis() / 1000L
        track = Track(
                artistName,
                trackName,
                albumName,
                presenter.getTrackDuration(artistName, trackName),
                timeStamp,
                ""
        )
        presenter.getAlbumArtWork(albumName, artistName, trackName)
    }

    // ステータスバーから通知が消去される
    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        super.onNotificationRemoved(sbn)
    }

    override fun sendTrackInfoToReceiver(albumArtWork: String) {
        Log.i("sendTrackInfo", "Called")
        val intent = Intent("AppleMusic")
        intent.putExtra("artistName", track!!.artistName)
        intent.putExtra("trackName", track!!.name)
        intent.putExtra("albumName", track!!.albumName)
        intent.putExtra("playingTime", track!!.playingTime)
        intent.putExtra("timeStamp", track!!.timeStamp)
        intent.putExtra("albumArtWork", albumArtWork)
        println("Updated: ${intent.getStringExtra("albumArtWork")}")
        sendBroadcast(intent)
    }

    private fun setPreviousTrackName(trackName: String) {
        val data = getSharedPreferences("DATA", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = data.edit()
        editor.putString("PreviousTrackName", trackName)
        editor.apply()
    }
}