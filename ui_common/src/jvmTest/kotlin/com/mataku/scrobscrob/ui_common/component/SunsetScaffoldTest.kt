package com.mataku.scrobscrob.ui_common.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetScaffold
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetText
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetTopAppBar
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("VRT")
class SunsetScaffoldTest {
  @Test
  fun layout() {
    captureScreenshot(
      appTheme = AppTheme.DARK,
      content = { Content() },
      fileName = "sunset_scaffold.png",
    )
  }

  @Test
  fun layout_light() {
    captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = { Content() },
      fileName = "sunset_scaffold_light.png",
    )
  }
}

@Composable
private fun Content() {
  SunsetScaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      SunsetTopAppBar(title = { SunsetText.Title(text = "Scaffold") })
    },
  ) { padding ->
    SunsetText.Body(
      text = "Scaffold content",
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .padding(16.dp),
    )
  }
}
