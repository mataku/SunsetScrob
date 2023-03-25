package com.mataku.scrobscrob.scrobble.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.mataku.scrobscrob.data.repository.NowPlayingRepository
import com.mataku.scrobscrob.data.repository.ScrobbleRepository
import com.mataku.scrobscrob.data.repository.ScrobbleSettingRepository
import com.mataku.scrobscrob.data.repository.TrackRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import org.koin.android.ext.android.inject

@AndroidEntryPoint
class MusicNotificationListenerService() : NotificationListenerService() {

  private var previousTrackName = ""

  private val nowPlayingRepository: NowPlayingRepository by inject()

  private val trackRepository: TrackRepository by inject()

  private val scrobbleRepository: ScrobbleRepository by inject()

  private val scrobbleSettingRepository: ScrobbleSettingRepository by inject()

  private var requester: MusicNotificationRequester? = null

  override fun onCreate() {
    super.onCreate()
    val job = Job()
    requester = MusicNotificationRequester(
      job,
      trackRepository,
      nowPlayingRepository,
      scrobbleRepository,
      scrobbleSettingRepository
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
