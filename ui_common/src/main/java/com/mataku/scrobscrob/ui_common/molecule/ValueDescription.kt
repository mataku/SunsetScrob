package com.mataku.scrobscrob.ui_common.molecule

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview

@Composable
fun ValueDescription(
  value: String,
  label: String
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = value,
      style = SunsetTextStyle.body.copy(
        fontWeight = FontWeight.Medium
      ),
      maxLines = 1,
      overflow = TextOverflow.Ellipsis
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
      text = label,
      style = SunsetTextStyle.caption,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun ValueDescriptionPreview() {
  SunsetThemePreview {
    Surface {
      ValueDescription(
        value = "10000",
        label = "listeners"
      )
    }
  }
}
