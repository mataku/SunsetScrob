package com.mataku.scrobscrob.ui_common.extension

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class DurationExtensionSpec : DescribeSpec({
  describe("toReadableString") {
    context("expected response") {
      it("should parse as minutes and seconds") {
        val duration = "214"

        duration.toReadableString() shouldBe "3:34"
      }

      it("should parse as minutes and zero padding seconds") {
        val duration = "181"

        duration.toReadableString() shouldBe "3:01"
      }
    }

    context("unexpected response") {
      it("should return null") {
        val duration = "hoge"

        duration.toReadableString() shouldBe null
      }
    }
  }
})
