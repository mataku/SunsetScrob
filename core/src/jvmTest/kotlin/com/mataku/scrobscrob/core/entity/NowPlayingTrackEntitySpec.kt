package com.mataku.scrobscrob.core.entity

import io.kotest.core.annotation.Ignored
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.mockk.every
import io.mockk.mockkStatic

@Ignored
class NowPlayingTrackEntitySpec : DescribeSpec({

  beforeSpec {
    mockkStatic(System::class)
  }

  describe("#overScrobblePoint") {
    context("over half of duration") {
      val entity = NowPlayingTrackEntity(
        duration = 300000L,
        timeStamp = 1000000000000L
      )
      every {
        System.currentTimeMillis()
      }.returns(1000000000000L + entity.duration / 2 + 1L)

      it("should return true") {
        entity.overScrobblePoint().shouldBeTrue()
      }
    }

    context("not over half of duration") {
      val entity = NowPlayingTrackEntity(
        duration = 300000L,
        timeStamp = 1000000000000L
      )
      every {
        System.currentTimeMillis()
      }.returns(1000000000000L + entity.duration / 2 - 1L)

      it("should return true") {
        entity.overScrobblePoint().shouldBeFalse()
      }
    }
  }
})
