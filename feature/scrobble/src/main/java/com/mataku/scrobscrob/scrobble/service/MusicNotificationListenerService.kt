package com.mataku.scrobscrob.scrobble.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.mataku.scrobscrob.data.repository.NowPlayingRepository
import com.mataku.scrobscrob.data.repository.ScrobbleRepository
import com.mataku.scrobscrob.data.repository.ScrobbleSettingRepository
import com.mataku.scrobscrob.data.repository.TrackRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import javax.inject.Inject

@AndroidEntryPoint
class MusicNotificationListenerService() : NotificationListenerService() {

  private var previousTrackName = ""

  @Inject
  lateinit var nowPlayingRepository: NowPlayingRepository

  @Inject
  lateinit var trackRepository: TrackRepository

  @Inject
  lateinit var scrobbleRepository: ScrobbleRepository

  @Inject
  lateinit var scrobbleSettingRepository: ScrobbleSettingRepository

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
    bundle.get("android.mediaSession") ?: return

    val trackName = bundle.get("android.title")?.toString() ?: return
    val artistName = bundle.get("android.text")?.toString() ?: return

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
