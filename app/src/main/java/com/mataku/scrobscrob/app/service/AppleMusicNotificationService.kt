package com.mataku.scrobscrob.app.service

import android.content.pm.PackageManager
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.mataku.scrobscrob.BuildConfig
import com.mataku.scrobscrob.app.model.Scrobble
import com.mataku.scrobscrob.app.model.Track
import com.mataku.scrobscrob.app.presenter.AppleMusicNotificationServicePresenter
import com.mataku.scrobscrob.app.ui.view.NotificationServiceInterface
import com.mataku.scrobscrob.app.util.SharedPreferencesHelper
import io.realm.Realm


class AppleMusicNotificationService : NotificationListenerService(), NotificationServiceInterface {
    private val APPLE_MUSIC_PACKAGE_NAME = "com.apple.android.music"
    private val presenter = AppleMusicNotificationServicePresenter(this)
    private val sharedPreferencesHelper = SharedPreferencesHelper(this)
    private lateinit var track: Track

    override fun onCreate() {
        super.onCreate()
        try {
            val appleMusicPackageInfo = packageManager.getPackageInfo(APPLE_MUSIC_PACKAGE_NAME, 0)
            if (BuildConfig.DEBUG) {
                Log.i("AppleMusicNotification", "Apple music is installed! (version: ${appleMusicPackageInfo.versionCode}")
            }
        } catch (e: PackageManager.NameNotFoundException) {
            if (BuildConfig.DEBUG) {
                Log.i("AppleMusicNotification", "Apple music is NOT installed!")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    // ステータスバーに通知が更新される
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)
        val sessionKey = sharedPreferencesHelper.getSessionKey()
        val previousTrackName = sharedPreferencesHelper.getPreviousTrackName()

        // Ignore if not Apple Music APP
        if (sbn.packageName != APPLE_MUSIC_PACKAGE_NAME) {
            Log.i("Notification", "Not apple music notification")
            return
        }

        // Do not request when not logged in
        if (sessionKey.isEmpty()) {
            Log.i("INFO", "session key is empty!")
            return
        }

        val extra = sbn.notification.extras

        // track name
        // Format: android.title=songName
        val extraTitle = extra.get("android.title")
        extraTitle ?: return
        val trackName = extraTitle.toString()

        if (previousTrackName == trackName) {
            Log.i("INFO", "Same Track!")
            return
        }

        sharedPreferencesHelper.setPreviousTrackName(trackName)

        if (sharedPreferencesHelper.overScrobblingPoint()) {
            presenter.scrobble(
                    track,
                    sessionKey,
                    sharedPreferencesHelper.getTimeStamp()
            )
        }

        sharedPreferencesHelper.setPreviousTrackName(trackName)
        // artist name and album name
        // Format: android.text=artistName - albumName
        // e.g. PassCode — VIRTUAL
        val albumInfo = extra.get("android.text")
        albumInfo ?: return

        val array = albumInfo.toString().split(" — ".toRegex(), 2).dropLastWhile { it.isEmpty() }.toTypedArray()

        // e.g. Track Downloading Notification
        if (array.size <= 1) {
            return
        }
        val artistName = array[0].trim()
        val albumName = array[1].trim()

        sharedPreferencesHelper.setTimeStamp()
        track = Track(
                artistName,
                trackName,
                albumName
        )
        presenter.getTrackInfo(track, sessionKey)
    }

    // ステータスバーから通知が消去される
    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        super.onNotificationRemoved(sbn)
    }

    override fun saveScrobble(track: Track) {
        var realm = Realm.getDefaultInstance();
        realm.executeTransaction {
            var scrobble = realm.createObject(Scrobble::class.java, Scrobble().count() + 1)
            scrobble.albumName = track.albumName
            scrobble.artistName = track.artistName
            scrobble.artwork = sharedPreferencesHelper.getAlbumArtWork()
            scrobble.trackName = track.name
        }
    }

    override fun setTrackPlayingTime(playingTime: Long) {
        sharedPreferencesHelper.setPLayingTime(playingTime)
    }

    override fun setAlbumArtwork(albumArtwork: String) {
        sharedPreferencesHelper.setAlbumArtwork(albumArtwork)
    }
}