package com.mataku.scrobscrob.core.entity

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class AppThemeSpec : DescribeSpec({
    describe("primaryId") {
        it("should unique") {
            val appThemes = AppTheme.values()
            val primaryIds = appThemes.map {
                it.primaryId
            }
            primaryIds.size shouldBe appThemes.size
        }
    }

    describe("priority") {
        it("should unique") {
            val appThemes = AppTheme.values()
            val priorityList = appThemes.map {
                it.priority
            }
            priorityList.size shouldBe appThemes.size
        }
    }

    describe("#find") {
        context("null is passed") {
            it("should return default value as DARK") {
                AppTheme.find(null) shouldBe AppTheme.DARK
            }
        }
        context("NotFound id") {
            it("should return default value as DARK") {
                AppTheme.find(0) shouldBe AppTheme.DARK
            }
        }
        context("id exists") {
            it("should return corresponding AppTheme") {
                AppTheme.find(AppTheme.LIGHT.primaryId) shouldBe AppTheme.LIGHT
            }
        }
    }
})