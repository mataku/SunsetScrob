package com.mataku.scrobscrob.app.service

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.widget.RemoteViews
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.app.model.Scrobble
import com.mataku.scrobscrob.app.model.Track
import com.mataku.scrobscrob.app.model.entity.RxEventBus
import com.mataku.scrobscrob.app.model.entity.UpdateNowPlayingEvent
import com.mataku.scrobscrob.app.model.entity.UpdateScrobbledListEvent
import com.mataku.scrobscrob.app.presenter.AppleMusicNotificationServicePresenter
import com.mataku.scrobscrob.app.ui.view.NotificationServiceInterface
import com.mataku.scrobscrob.app.util.AppUtil
import com.mataku.scrobscrob.app.util.SharedPreferencesHelper
import io.realm.Realm
import jp.yokomark.remoteview.reader.RemoteViewsReader
import jp.yokomark.remoteview.reader.action.ReflectionAction


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
            appUtil.debugLog("AppleMusicNotification", "Apple music is installed! (version: ${appleMusicPackageInfo.longVersionCode}")
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
        val sessionKey = sharedPreferences.getString("SessionKey", "")
        val sharedPreferencesHelper = SharedPreferencesHelper(this)
        var trackName = ""
        var artistName = ""

        // Ignore if not Apple Music APP
        if (sbn.packageName != APPLE_MUSIC_PACKAGE_NAME) {
            appUtil.debugLog("Notification", "Not apple music notification")
            return
        }

        // Do not request when not logged in
        if (sessionKey.isEmpty()) {
            return
        }

        val contentView = sbn.notification.bigContentView as RemoteViews
        val info = RemoteViewsReader.read(this, contentView)
        info.actions.forEachIndexed { index, action ->
            if (action is ReflectionAction) {
                when (index) {
                    0 -> {
                        trackName = action.value.toString().trim()
                    }
                    1 -> {
                        artistName = action.value.toString().trim()
                    }
                }
            }
        }

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