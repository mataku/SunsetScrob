package com.mataku.scrobscrob.ui_common.organism

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.ui_common.SunsetTextStyle

@Composable
fun ContentHeader(
  text: String
) {
  Surface(
    shadowElevation = 2.dp
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          MaterialTheme.colorScheme.background
        )
    ) {
      Text(
        text = text,
        style = SunsetTextStyle.h6,
        modifier = Modifier
          .padding(16.dp)
      )
    }
  }
}
