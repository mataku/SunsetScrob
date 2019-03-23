package com.mataku.scrobscrob.app.service

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.text.TextUtils
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.app.model.RxEventBus
import com.mataku.scrobscrob.app.presenter.AppleMusicNotificationServicePresenter
import com.mataku.scrobscrob.app.ui.view.NotificationServiceInterface
import com.mataku.scrobscrob.app.util.AppUtil
import com.mataku.scrobscrob.app.util.SharedPreferencesHelper
import com.mataku.scrobscrob.core.entity.Scrobble
import com.mataku.scrobscrob.core.entity.Track
import com.mataku.scrobscrob.core.entity.UpdateNowPlayingEvent
import com.mataku.scrobscrob.core.entity.UpdateScrobbledListEvent
import io.realm.Realm

class AppleMusicNotificationService : NotificationListenerService(), NotificationServiceInterface {
    private val APPLE_MUSIC_PACKAGE_NAME = "com.apple.android.music"
    private val presenter = AppleMusicNotificationServicePresenter(this)
    private lateinit var track: Track
    private var previousTrackName: String = ""
    private val appUtil = AppUtil()

    override fun onCreate() {
        super.onCreate()
        RxEventBus.post(UpdateNowPlayingEvent(dummyTrack()))

        try {
            val appleMusicPackageInfo = packageManager.getPackageInfo(APPLE_MUSIC_PACKAGE_NAME, 0)
            val versionCode: Any = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                appleMusicPackageInfo.longVersionCode
            } else {
                appleMusicPackageInfo.versionCode
            }
            appUtil.debugLog(
                "AppleMusicNotification",
                "Apple music is installed! (version: $versionCode)"
            )
        } catch (e: PackageManager.NameNotFoundException) {
            appUtil.debugLog("AppleMusicNotification", "Apple music is NOT installed!")
        }
    }

    override fun onDestroy() {
        presenter.dispose()
        super.onDestroy()
    }

    // ステータスバーに通知が更新される
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)
        val sharedPreferences = getSharedPreferences("DATA", Context.MODE_PRIVATE)
        val sessionKey = sharedPreferences.getString("SessionKey", "") ?: ""
        if (sessionKey.isBlank()) {
            return
        }

        val sharedPreferencesHelper = SharedPreferencesHelper(this)
        var trackName: String?
        var artistName: String?

        // Ignore if not Apple Music APP
        if (sbn.packageName != APPLE_MUSIC_PACKAGE_NAME) {
            appUtil.debugLog("Notification", "Not apple music notification")
            return
        }

        // Do not request when not logged in
        if (TextUtils.isEmpty(sessionKey)) {
            return
        }

        val notification = sbn.notification
        val bundle = notification?.extras ?: return

        trackName = bundle.get("android.title")?.toString() ?: return
        artistName = bundle.get("android.text")?.toString() ?: return

        if (previousTrackName == trackName) {
            return
        } else {
            if (sharedPreferencesHelper.overScrobblingPoint()) {
                if (!this::track.isInitialized) {
                    return
                }
                presenter.scrobble(
                    track,
                    sessionKey,
                    sharedPreferencesHelper.getTimeStamp()
                )
                appUtil.debugLog("ScrobbleApi", "called")
            } else {
                appUtil.debugLog("ScrobbleApi", "not called")
            }
        }

        previousTrackName = trackName
        sharedPreferencesHelper.setTimeStamp()
        presenter.getTrackInfo(trackName, artistName, sessionKey)
        val intent = Intent("AppleMusic")
        sendBroadcast(intent)
    }

    // ステータスバーから通知が消去される
    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        super.onNotificationRemoved(sbn)
    }

    override fun notifyNowPlayingUpdated(track: Track) {
        this.track = track
        RxEventBus.post(UpdateNowPlayingEvent(track))
    }

    override fun setAlbumArtwork(albumArtWork: String) {
        val sharedPreferencesHelper = SharedPreferencesHelper(this)
        sharedPreferencesHelper.setAlbumArtwork(albumArtWork)
    }

    override fun saveScrobble(track: Track) {
        val realm = Realm.getDefaultInstance()
        val sharedPreferencesHelper = SharedPreferencesHelper(this)

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
