package com.mataku.scrobscrob.scrobble.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.mataku.scrobscrob.data.repository.di.ScrobbleServiceDependencies
import kotlinx.coroutines.Job

class MusicNotificationListenerService : NotificationListenerService() {

  private var previousTrackName = ""

  private var requester: MusicNotificationRequester? = null

  override fun onCreate() {
    super.onCreate()
    val dependencies = application as ScrobbleServiceDependencies
    val job = Job()
    requester = MusicNotificationRequester(
      job,
      dependencies.trackRepository,
      dependencies.nowPlayingRepository,
      dependencies.scrobbleRepository,
      dependencies.scrobbleSettingRepository
    )
  }

  override fun onNotificationPosted(sbn: StatusBarNotification) {
    super.onNotificationPosted(sbn)
    val allowedPackages = requester?.state?.allowedApps ?: emptySet()

    val notification = sbn.notification
    val bundle = notification?.extras ?: return

    if (allowedPackages.isEmpty() || !allowedPackages.contains(sbn.packageName)) {
      return
    }
    // Notification using Media Player
    bundle.getString("android.mediaSession") ?: return

    val trackName = bundle.getString("android.title") ?: return
    val artistName = bundle.getString("android.text") ?: return

    if (trackName == previousTrackName) {
      return
    }


    previousTrackName = trackName
    requester?.updateNowPlaying(
      trackName = trackName,
      artistName = artistName
    )
  }

  override fun onDestroy() {
    requester?.dispose()
    requester = null
    super.onDestroy()
  }
}
