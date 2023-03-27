package com.mataku.scrobscrob.ui_common.organism

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.ui_common.SunsetTextStyle

@Composable
fun FilteringBottomSheet() {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(
        horizontal = 16.dp,
        vertical = 24.dp
      )
  ) {
    Text(
      text = "Filter list",
      style = SunsetTextStyle.h7
    )
  }
}
