package com.mataku.scrobscrob.ui_common.navigation

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe

class SunsetNavBackStackSpec : DescribeSpec({
  describe("SunsetNavBackStack") {
    it("starts with the initial key on top") {
      val backStack = SunsetNavBackStack(initial = LoginKey)
      backStack.entries.last().key shouldBe LoginKey
      backStack.isEmpty().shouldBeFalse()
    }

    it("isEmpty returns true after popping the only entry") {
      val backStack = SunsetNavBackStack(initial = LoginKey)
      backStack.entries.removeAt(backStack.entries.lastIndex)
      backStack.isEmpty().shouldBeTrue()
    }
  }
})
