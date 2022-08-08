package com.mataku.scrobscrob.scrobble.service

import com.mataku.scrobscrob.data.repository.NowPlayingRepository
import com.mataku.scrobscrob.data.repository.TrackRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MusicNotificationRequester(
  private val job: Job,
  private val trackRepository: TrackRepository,
  private val nowPlayingRepository: NowPlayingRepository
) {

  private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO + job)

  private var isLoading = false

  private var previousTrackName: String = ""

  fun updateNowPlaying(
    trackName: String,
    artistName: String
  ) {
    if (isLoading || previousTrackName == trackName) {
      return
    }

    previousTrackName = trackName

    coroutineScope.launch {
      val currentNowPlaying = nowPlayingRepository.current()
      if (currentNowPlaying?.trackName == trackName) {
        return@launch
      }

      trackRepository.getInfo(
        trackName = trackName,
        artistName = artistName
      )
        .onStart {
          isLoading = true
        }
        .catch {
          isLoading = false
        }.collect {
          nowPlayingRepository.update(it)
            .catch { e ->
            }
            .collect {
              previousTrackName = trackName
              isLoading = false
            }
        }
    }
  }

  fun dispose() {
    job.cancel()
  }
}
