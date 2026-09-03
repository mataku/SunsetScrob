package com.mataku.scrobscrob.auth.webauth

import app.cash.turbine.test
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class WebAuthCallbackChannelSpec : DescribeSpec({
  describe("#offer") {
    it("delivers a token offered before collection starts") {
      val channel = WebAuthCallbackChannel()
      channel.offer("abc123")
      channel.tokens.test {
        awaitItem() shouldBe "abc123"
        expectNoEvents()
      }
    }

    it("delivers a token offered while collecting") {
      val channel = WebAuthCallbackChannel()
      channel.tokens.test {
        channel.offer("abc123")
        awaitItem() shouldBe "abc123"
        expectNoEvents()
      }
    }

    it("keeps only the latest token when nobody is collecting") {
      val channel = WebAuthCallbackChannel()
      channel.offer("first")
      channel.offer("second")
      channel.tokens.test {
        awaitItem() shouldBe "second"
        expectNoEvents()
      }
    }

    it("does not replay a consumed token to a new collector") {
      val channel = WebAuthCallbackChannel()
      channel.offer("abc123")
      channel.tokens.test {
        awaitItem() shouldBe "abc123"
        cancelAndIgnoreRemainingEvents()
      }
      channel.tokens.test {
        expectNoEvents()
      }
    }
  }
})
