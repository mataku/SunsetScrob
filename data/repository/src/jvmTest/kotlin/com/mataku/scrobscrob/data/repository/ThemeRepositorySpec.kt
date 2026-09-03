package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.data.db.ThemeDataStore
import com.mataku.scrobscrob.data.db.entity.AppThemeEntity
import com.mataku.scrobscrob.data.repository.mapper.toAppTheme
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf

class ThemeRepositorySpec : DescribeSpec({
  extension(com.mataku.scrobscrob.test_helper.unit.CoroutinesListener())

  val themeDataStore = mockk<ThemeDataStore>()
  val repository = ThemeRepositoryImpl(themeDataStore)

  describe("currentTheme") {
    it("should return theme") {
      coEvery {
        themeDataStore.theme()
      }.returns(flowOf(AppThemeEntity.LASTFM_DARK))

      repository.currentTheme().collect {
        it shouldBe AppTheme.LASTFM_DARK
      }

      coVerify(exactly = 1) {
        themeDataStore.theme()
      }
    }
  }

  describe("storeTheme") {
    val theme = AppThemeEntity.LASTFM_DARK

    it("should call ThemeDataStore#setTheme") {
      coEvery {
        themeDataStore.setTheme(theme)
      }.returns(flowOf(Unit))

      repository.storeTheme(theme.toAppTheme()).collect()

      coVerify(exactly = 1) {
        themeDataStore.setTheme(theme)
      }
    }
  }
})
