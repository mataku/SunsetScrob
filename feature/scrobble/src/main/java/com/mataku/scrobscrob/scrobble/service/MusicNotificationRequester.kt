package com.mataku.scrobscrob.scrobble.service

import com.mataku.scrobscrob.data.repository.NowPlayingRepository
import com.mataku.scrobscrob.data.repository.ScrobbleRepository
import com.mataku.scrobscrob.data.repository.ScrobbleSettingRepository
import com.mataku.scrobscrob.data.repository.TrackRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MusicNotificationRequester(
  private val job: Job,
  private val trackRepository: TrackRepository,
  private val nowPlayingRepository: NowPlayingRepository,
  private val scrobbleRepository: ScrobbleRepository,
  private val scrobbleSettingRepository: ScrobbleSettingRepository
) {

  private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO + job)

  private var isLoading = false

  private var previousTrackName: String = ""

  var state = State()
    private set

  init {
    coroutineScope.launch {
      scrobbleSettingRepository.allowedAppsFlow()
        .distinctUntilChanged()
        .catch { }
        .collect {
          state = state.copy(allowedApps = it)
        }
    }
  }

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
          currentNowPlaying?.let {
            scrobbleRepository.scrobble().catch {}.collect()
          }

          nowPlayingRepository.update(it)
            .catch {}
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

  data class State(
    val allowedApps: Set<String> = emptySet()
  )
}
