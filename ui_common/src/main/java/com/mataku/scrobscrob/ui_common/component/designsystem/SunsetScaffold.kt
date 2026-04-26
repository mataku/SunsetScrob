package com.mataku.scrobscrob.ui_common.component.designsystem

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview

@Composable
fun SunsetScaffold(
  modifier: Modifier = Modifier,
  topBar: @Composable () -> Unit = {},
  bottomBar: @Composable () -> Unit = {},
  snackbarHost: @Composable () -> Unit = {},
  floatingActionButton: @Composable () -> Unit = {},
  content: @Composable (PaddingValues) -> Unit,
) {
  Scaffold(
    modifier = modifier,
    topBar = topBar,
    bottomBar = bottomBar,
    snackbarHost = snackbarHost,
    floatingActionButton = floatingActionButton,
    content = content,
  )
}

@Preview
@ShowkaseComposable(name = "SunsetScaffold", group = "Design system")
@Composable
internal fun SunsetScaffoldPreview() {
  SunsetThemePreview {
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
}
