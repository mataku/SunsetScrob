package com.mataku.scrobscrob.ui_common.organism

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.ui_common.SunsetTextStyle

@Composable
fun ContentHeader(
  text: String
) {
  Text(
    text = text,
    style = SunsetTextStyle.h6,
    modifier = Modifier
      .fillMaxWidth()
      .background(MaterialTheme.colors.background)
      .padding(16.dp)
  )
  Divider()
}
