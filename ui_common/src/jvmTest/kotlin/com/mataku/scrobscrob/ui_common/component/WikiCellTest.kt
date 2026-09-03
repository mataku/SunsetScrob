package com.mataku.scrobscrob.ui_common.component

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.core.entity.Wiki
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("VRT")
class WikiCellTest {
  private val wiki = Wiki(
    published = "01 January 2023",
    content = "",
    summary = "\"Clocks\" emerged in <b>conception during the late stages</b> into the production of Coldplay's second album.<br>Tom &amp; Jerry <i>liked</i> it. <a href=\"http://www.last.fm/music/Coldplay/_/Clocks\">Read more on Last.fm</a>.",
  )

  @Test
  fun layout() {
    captureScreenshot(
      appTheme = AppTheme.DARK,
      content = { WikiCell(wiki = wiki, name = "Clocks", modifier = Modifier.padding(16.dp), onUrlTap = {}) },
      fileName = "wiki_cell.png",
    )
  }

  @Test
  fun layout_light() {
    captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = { WikiCell(wiki = wiki, name = "Clocks", modifier = Modifier.padding(16.dp), onUrlTap = {}) },
      fileName = "wiki_cell_light.png",
    )
  }
}
