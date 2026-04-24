package com.mataku.scrobscrob.data.repository.di

import com.mataku.scrobscrob.data.repository.NowPlayingRepository
import com.mataku.scrobscrob.data.repository.ScrobbleRepository
import com.mataku.scrobscrob.data.repository.ScrobbleSettingRepository
import com.mataku.scrobscrob.data.repository.TrackRepository

interface ScrobbleServiceDependencies {
  val nowPlayingRepository: NowPlayingRepository
  val trackRepository: TrackRepository
  val scrobbleRepository: ScrobbleRepository
  val scrobbleSettingRepository: ScrobbleSettingRepository
}
