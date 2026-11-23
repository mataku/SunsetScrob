package com.mataku.scrobscrob.scrobble.service

import com.mataku.scrobscrob.core.entity.NowPlayingTrackEntity
import com.mataku.scrobscrob.core.entity.ScrobbleResult
import com.mataku.scrobscrob.core.entity.TrackArtist
import com.mataku.scrobscrob.core.entity.TrackInfo
import com.mataku.scrobscrob.data.repository.NowPlayingRepository
import com.mataku.scrobscrob.data.repository.ScrobbleRepository
import com.mataku.scrobscrob.data.repository.ScrobbleSettingRepository
import com.mataku.scrobscrob.data.repository.TrackRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher

class MusicNotificationRequesterSpec : DescribeSpec({
  describe("init") {
    it("exposes the allowed apps from the scrobble settings") {
      val fixture = Fixture()
      coEvery { fixture.scrobbleSettingRepository.allowedAppsFlow() } returns
        flowOf(setOf("com.spotify.music"))

      fixture.requester().state.allowedApps shouldBe setOf("com.spotify.music")
    }
  }

  describe("#updateNowPlaying") {
    context("nothing is playing yet") {
      it("stores the fetched track as now playing without scrobbling") {
        val fixture = Fixture()

        fixture.requester().updateNowPlaying(trackName = "Drama", artistName = "aespa")

        coVerify(exactly = 1) { fixture.trackRepository.getInfo("Drama", "aespa") }
        coVerify(exactly = 1) { fixture.nowPlayingRepository.update(drama) }
        coVerify(exactly = 0) { fixture.scrobbleRepository.scrobble(any()) }
      }
    }

    context("another track is playing") {
      it("scrobbles the previous track before replacing now playing") {
        val fixture = Fixture()
        every { fixture.nowPlayingRepository.current() } returns flowOf(supernova)

        fixture.requester().updateNowPlaying(trackName = "Drama", artistName = "aespa")

        coVerifyOrder {
          fixture.scrobbleRepository.scrobble(supernova)
          fixture.nowPlayingRepository.update(drama)
        }
      }

      it("still replaces now playing when the scrobble fails") {
        val fixture = Fixture()
        every { fixture.nowPlayingRepository.current() } returns flowOf(supernova)
        coEvery { fixture.scrobbleRepository.scrobble(any()) } returns flow { error("offline") }

        fixture.requester().updateNowPlaying(trackName = "Drama", artistName = "aespa")

        coVerify(exactly = 1) { fixture.nowPlayingRepository.update(drama) }
      }
    }

    context("the notified track is already now playing") {
      it("does not fetch the track again") {
        val fixture = Fixture()
        every { fixture.nowPlayingRepository.current() } returns flowOf(supernova)

        fixture.requester().updateNowPlaying(trackName = "Supernova", artistName = "aespa")

        coVerify(exactly = 0) { fixture.trackRepository.getInfo(any(), any()) }
        coVerify(exactly = 0) { fixture.scrobbleRepository.scrobble(any()) }
      }
    }

    // A media notification is re-posted on every play/pause and progress update.
    context("the same track is notified twice in a row") {
      it("handles it once") {
        val fixture = Fixture()
        val requester = fixture.requester()

        requester.updateNowPlaying(trackName = "Drama", artistName = "aespa")
        requester.updateNowPlaying(trackName = "Drama", artistName = "aespa")

        coVerify(exactly = 1) { fixture.trackRepository.getInfo("Drama", "aespa") }
      }
    }

    context("fetching the track fails") {
      it("still handles the next track") {
        val fixture = Fixture()
        coEvery { fixture.trackRepository.getInfo("Drama", "aespa") } returns flow { error("offline") }
        val requester = fixture.requester()

        requester.updateNowPlaying(trackName = "Drama", artistName = "aespa")
        requester.updateNowPlaying(trackName = "Whiplash", artistName = "aespa")

        coVerify(exactly = 0) { fixture.nowPlayingRepository.update(drama) }
        coVerify(exactly = 1) { fixture.nowPlayingRepository.update(whiplash) }
      }
    }
  }

  describe("#dispose") {
    it("cancels the job passed in by the service") {
      val fixture = Fixture()

      fixture.requester().dispose()

      fixture.job.isCancelled.shouldBeTrue()
    }
  }
})

private val drama = trackInfo("Drama")
private val whiplash = trackInfo("Whiplash")
private val supernova = NowPlayingTrackEntity(
  artistName = "aespa",
  trackName = "Supernova",
  timeStamp = 0L,
)

private fun trackInfo(name: String) = TrackInfo(
  artist = TrackArtist(name = "aespa", url = ""),
  listeners = "100",
  playCount = "1000",
  url = "",
  name = name,
)

private class Fixture {
  val job = Job()
  val trackRepository = mockk<TrackRepository>()
  val nowPlayingRepository = mockk<NowPlayingRepository>()
  val scrobbleRepository = mockk<ScrobbleRepository>()
  val scrobbleSettingRepository = mockk<ScrobbleSettingRepository>()

  init {
    coEvery { scrobbleSettingRepository.allowedAppsFlow() } returns flowOf(emptySet())
    every { nowPlayingRepository.current() } returns flowOf(null)
    coEvery { nowPlayingRepository.update(any()) } returns flowOf(Unit)
    coEvery { scrobbleRepository.scrobble(any()) } returns flowOf(ScrobbleResult(accepted = true))
    coEvery { trackRepository.getInfo("Drama", "aespa") } returns flowOf(drama)
    coEvery { trackRepository.getInfo("Whiplash", "aespa") } returns flowOf(whiplash)
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  fun requester() = MusicNotificationRequester(
    job = job,
    trackRepository = trackRepository,
    nowPlayingRepository = nowPlayingRepository,
    scrobbleRepository = scrobbleRepository,
    scrobbleSettingRepository = scrobbleSettingRepository,
    dispatcher = UnconfinedTestDispatcher(),
  )
}
