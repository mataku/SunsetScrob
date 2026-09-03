package com.mataku.scrobscrob.ui_common.component.designsystem

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import com.mataku.scrobscrob.ui_common.style.backgroundColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SunsetBottomSheet(
  sheetContent: @Composable ColumnScope.() -> Unit,
  sheetPeekHeight: Dp,
  modifier: Modifier = Modifier,
  sheetContainerColor: Color = LocalAppTheme.current.backgroundColor(),
  content: @Composable (PaddingValues) -> Unit,
) {
  val scaffoldState = rememberBottomSheetScaffoldState(
    bottomSheetState = rememberStandardBottomSheetState(
      initialValue = SheetValue.PartiallyExpanded,
    ),
  )
  BottomSheetScaffold(
    modifier = modifier,
    scaffoldState = scaffoldState,
    sheetContainerColor = sheetContainerColor,
    containerColor = LocalAppTheme.current.backgroundColor(),
    sheetPeekHeight = sheetPeekHeight,
    sheetContent = sheetContent,
    content = content,
  )
}

@Preview
@Composable
internal fun SunsetBottomSheetPreview() {
  SunsetThemePreview {
    SunsetBottomSheet(
      sheetContent = {
        SunsetText.Body(
          text = "Sheet content",
          modifier = Modifier.padding(16.dp),
        )
      },
      sheetPeekHeight = 160.dp,
      modifier = Modifier.fillMaxWidth().height(360.dp)
    ) { padding ->
      SunsetText.Body(
        text = "Main content",
        modifier = Modifier
          .fillMaxSize()
          .padding(padding)
          .padding(16.dp),
      )
    }
  }
}
