package com.mataku.scrobscrob.ui_common.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.backgroundColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SunsetBottomSheet(
  sheetContent: @Composable ColumnScope.() -> Unit,
  sheetPeekHeight: Dp,
  modifier: Modifier = Modifier,
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
    sheetContainerColor = LocalAppTheme.current.backgroundColor(),
    containerColor = LocalAppTheme.current.backgroundColor(),
    sheetPeekHeight = sheetPeekHeight,
    sheetContent = sheetContent,
    content = content,
  )
}
