package com.mataku.scrobscrob.core.entity.presentation

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class IntRepresentationSpec : DescribeSpec({
  describe("toReadableIntValue") {
    it("keeps small numbers as is") {
      123.toReadableIntValue() shouldBe "123"
    }

    it("compacts thousands with K") {
      1500.toReadableIntValue() shouldBe "2K"
      "1500".toReadableIntValue() shouldBe "2K"
    }

    it("compacts millions with M") {
      10_000_000.toReadableIntValue() shouldBe "10M"
    }

    it("returns the input when it is not a number") {
      "abc".toReadableIntValue() shouldBe "abc"
    }
  }
})
