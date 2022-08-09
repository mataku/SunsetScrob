package com.mataku.scrobscrob.scrobble.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.mataku.scrobscrob.data.repository.NowPlayingRepository
import com.mataku.scrobscrob.data.repository.ScrobbleRepository
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

  private var requester: MusicNotificationRequester? = null

  override fun onCreate() {
    super.onCreate()
    val job = Job()
    requester = MusicNotificationRequester(
      job, trackRepository, nowPlayingRepository, scrobbleRepository
    )
  }

  override fun onNotificationPosted(sbn: StatusBarNotification) {
    super.onNotificationPosted(sbn)
    if (!ALLOW_PACKAGES.contains(sbn.packageName)) {
      return
    }
    val notification = sbn.notification
    val bundle = notification?.extras ?: return

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

  companion object {
    private const val SPOTIFY_PACKAGE = "com.spotify.music"
    private const val APPLE_MUSIC_PACKAGE = "com.apple.android.music"
    private val ALLOW_PACKAGES = listOf(SPOTIFY_PACKAGE, APPLE_MUSIC_PACKAGE)
  }
}
