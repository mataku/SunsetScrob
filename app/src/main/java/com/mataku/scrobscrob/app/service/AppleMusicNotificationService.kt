package com.mataku.scrobscrob.app.service

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.mataku.scrobscrob.BuildConfig
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.app.model.Scrobble
import com.mataku.scrobscrob.app.model.Track
import com.mataku.scrobscrob.app.model.entity.RxEventBus
import com.mataku.scrobscrob.app.model.entity.UpdateNowPlayingEvent
import com.mataku.scrobscrob.app.model.entity.UpdateScrobbledListEvent
import com.mataku.scrobscrob.app.presenter.AppleMusicNotificationServicePresenter
import com.mataku.scrobscrob.app.ui.view.NotificationServiceInterface
import com.mataku.scrobscrob.app.util.SharedPreferencesHelper
import io.realm.Realm


class AppleMusicNotificationService : NotificationListenerService(), NotificationServiceInterface {
    private val APPLE_MUSIC_PACKAGE_NAME = "com.apple.android.music"
    private val presenter = AppleMusicNotificationServicePresenter(this)
    private lateinit var track: Track
    private var previousTrackName: String = ""

    override fun onCreate() {
        super.onCreate()
        val sharedPreferencesHelper = SharedPreferencesHelper(this)

        try {
            val appleMusicPackageInfo = packageManager.getPackageInfo(APPLE_MUSIC_PACKAGE_NAME, 0)
            if (BuildConfig.DEBUG) {
                sharedPreferencesHelper.setPLayingTime(1000L)
                sharedPreferencesHelper.setTimeStamp()
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
        val sharedPreferences = getSharedPreferences("DATA", Context.MODE_PRIVATE)
        val sessionKey = sharedPreferences.getString("SessionKey", "")
        val sharedPreferencesHelper = SharedPreferencesHelper(this)

        // Ignore if not Apple Music APP
        if (sbn.packageName != APPLE_MUSIC_PACKAGE_NAME) {
            if (BuildConfig.DEBUG) {
                Log.i("Notification", "Not apple music notification")
            }
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
        } else {
            if (sharedPreferencesHelper.overScrobblingPoint()) {
                presenter.scrobble(
                        track,
                        sessionKey,
                        sharedPreferencesHelper.getTimeStamp()
                )
            }
            sharedPreferencesHelper.setPLayingTime(1000L)
            sharedPreferencesHelper.setTimeStamp()
            RxEventBus.post(UpdateNowPlayingEvent(dummyTrack()))
        }

        previousTrackName = trackName

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
        val intent = Intent("AppleMusic")
        sendBroadcast(intent)
    }

    // ステータスバーから通知が消去される
    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        super.onNotificationRemoved(sbn)
    }

    override fun notifyNowPlayingUpdated(track: Track) {
        RxEventBus.post(UpdateNowPlayingEvent(track))
    }

    override fun setAlbumArtwork(albumArtWork: String) {
        val sharedPreferencesHelper = SharedPreferencesHelper(this)
        sharedPreferencesHelper.setAlbumArtwork(albumArtWork)
    }

    override fun saveScrobble(track: Track) {
        val sharedPreferencesHelper = SharedPreferencesHelper(this)
        val realm = Realm.getDefaultInstance()

        realm.executeTransaction {
            val scrobble = realm.createObject(Scrobble::class.java, Scrobble().count() + 1)
            scrobble.albumName = track.albumName
            scrobble.artistName = track.artistName
            scrobble.artwork = sharedPreferencesHelper.getAlbumArtWork()
            scrobble.trackName = track.name
        }
        RxEventBus.post(UpdateScrobbledListEvent())
    }

    override fun setCurrentTrackInfo(playingTime: Long, albumArtWork: String) {
        val sharedPreferencesHelper = SharedPreferencesHelper(this)
        sharedPreferencesHelper.setPLayingTime(playingTime)
        sharedPreferencesHelper.setAlbumArtwork(albumArtWork)
    }

    private fun dummyTrack(): Track {
        return Track(
                getString(R.string.label_not_playing_message),
                getString(R.string.label_now_playing),
                getString(R.string.label_not_playing)
        )
    }
}