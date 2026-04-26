@file:OptIn(ExperimentalMaterial3Api::class)

package com.mataku.scrobscrob.ui_common

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.backgroundColor

@Stable
class SunsetModalBottomSheetState internal constructor(
  internal val materialState: SheetState,
) {
  val isVisible: Boolean
    get() = materialState.isVisible

  suspend fun hide() {
    materialState.hide()
  }

  suspend fun show() {
    materialState.show()
  }
}

@Composable
fun rememberSunsetModalBottomSheetState(
  skipPartiallyExpanded: Boolean = false,
): SunsetModalBottomSheetState {
  val state = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)
  return remember(state) { SunsetModalBottomSheetState(state) }
}

@Composable
fun SunsetModalBottomSheet(
  onDismissRequest: () -> Unit,
  sheetState: SunsetModalBottomSheetState = rememberSunsetModalBottomSheetState(),
  content: @Composable ColumnScope.() -> Unit,
) {
  ModalBottomSheet(
    onDismissRequest = onDismissRequest,
    containerColor = LocalAppTheme.current.backgroundColor(),
    sheetState = sheetState.materialState,
    contentWindowInsets = { WindowInsets.displayCutout },
    content = content,
  )
}
