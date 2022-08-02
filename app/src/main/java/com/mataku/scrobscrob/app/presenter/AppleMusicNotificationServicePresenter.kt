package com.mataku.scrobscrob.app.presenter

import com.mataku.scrobscrob.app.ui.view.NotificationServiceInterface
import com.mataku.scrobscrob.core.entity.Track
import com.mataku.scrobscrob.core.util.AppUtil
import com.mataku.scrobscrob.data.repository.NowPlayingRepository
import com.mataku.scrobscrob.data.repository.ScrobbleRepository
import com.mataku.scrobscrob.data.repository.TrackRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class AppleMusicNotificationServicePresenter(
  private val notificationServiceInterface: NotificationServiceInterface,
  private val nowPlayingRepository: NowPlayingRepository,
  private val trackRepository: TrackRepository,
  private val scrobbleRepository: ScrobbleRepository
) {

  companion object {
    private val appUtil = AppUtil()
    private val coroutineContext = Job() + Dispatchers.Main
  }

  fun dispose() {
    coroutineContext.cancel()
  }

  fun getTrackInfo(trackName: String, artistName: String, sessionKey: String) {
    CoroutineScope(coroutineContext).launch {
      setNowPlaying(trackName, artistName, sessionKey)
      getTrackInfo(artistName, trackName)
    }
  }

  fun scrobble(track: Track, sessionKey: String, timeStamp: Long) {
    CoroutineScope(coroutineContext).launch {
      requestScrobble(track, sessionKey, timeStamp)
    }
  }

  private suspend fun requestScrobble(track: Track, sessionKey: String, timeStamp: Long) {
//        scrobbleRepository.scrobble(track, sessionKey, timeStamp)
//            .onSuccess { attr ->
//                val accepted = attr.accepted
//                accepted?.let {
//                    if (it == 1) {
//                        notificationServiceInterface.saveScrobble(track)
//                        appUtil.debugLog("scrobbleApi", "success")
//                    }
//                }
//            }
//            .onFailure {
//                appUtil.debugLog("Scrobble API", it.localizedMessage.toString())
//            }
  }

  private suspend fun setNowPlaying(trackName: String, artistName: String, sessionKey: String) {
//        nowPlayingRepository.update(trackName, artistName, sessionKey)
//            .onSuccess {
//                notificationServiceInterface.notifyNowPlayingUpdated(
//                    Track(
//                        it.artist.text,
//                        it.track.text,
//                        it.album.text
//                    )
//                )
//
//            }
//            .onFailure {
//                appUtil.debugLog("NowPlayingApi", "Something wrong")
//            }
  }

  private suspend fun getTrackInfo(artistName: String, trackName: String) {
//        var trackDuration = appUtil.defaultPlayingTime
//        var albumArtwork = ""
//
//        trackRepository.getInfo(artistName, trackName)
//            .onSuccess {
//                val duration = it.duration
//                if (duration != null) {
//                    trackDuration = duration.toLong() / 1000L
//                }
//                try {
//                    it.album?.imageList?.let { list ->
//                        albumArtwork = list[1].imageUrl
//                    }
//                } catch (e: Exception) {
//                }
//                // Use default value if duration is 0
//                if (trackDuration == 0L) {
//                    trackDuration = appUtil.defaultPlayingTime
//                }
//
//                notificationServiceInterface.setCurrentTrackInfo(trackDuration, albumArtwork)
//            }
//            .onFailure {
//                notificationServiceInterface.setCurrentTrackInfo(trackDuration, albumArtwork)
//            }
  }
}
