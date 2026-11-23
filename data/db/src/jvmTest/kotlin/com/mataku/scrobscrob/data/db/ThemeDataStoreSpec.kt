package com.mataku.scrobscrob.data.db

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import app.cash.turbine.test
import com.mataku.scrobscrob.data.db.entity.AppThemeEntity
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first

class ThemeDataStoreSpec : DescribeSpec({
  describe("ThemeDataStore") {
    it("falls back to FOLLOW_SYSTEM when nothing has been stored") {
      withTestPreferencesDataStore { dataStore ->
        ThemeDataStore(dataStore).theme().first() shouldBe AppThemeEntity.FOLLOW_SYSTEM
      }
    }

    it("persists the theme passed to setTheme") {
      withTestPreferencesDataStore { dataStore ->
        val store = ThemeDataStore(dataStore)
        store.setTheme(AppThemeEntity.MIDNIGHT).test {
          awaitItem() shouldBe Unit
          awaitComplete()
        }

        store.theme().first() shouldBe AppThemeEntity.MIDNIGHT
      }
    }

    it("emits the new theme to theme collectors") {
      withTestPreferencesDataStore { dataStore ->
        val store = ThemeDataStore(dataStore)

        store.theme().test {
          awaitItem() shouldBe AppThemeEntity.FOLLOW_SYSTEM
          store.setTheme(AppThemeEntity.OCEAN)
          awaitItem() shouldBe AppThemeEntity.OCEAN
          cancelAndIgnoreRemainingEvents()
        }
      }
    }

    // A theme removed in a later version leaves its id behind in the user's preferences.
    it("falls back to FOLLOW_SYSTEM when the stored id matches no theme") {
      withTestPreferencesDataStore { dataStore ->
        dataStore.edit { it[intPreferencesKey("current_theme_id")] = 999 }

        ThemeDataStore(dataStore).theme().first() shouldBe AppThemeEntity.FOLLOW_SYSTEM
      }
    }
  }
})
