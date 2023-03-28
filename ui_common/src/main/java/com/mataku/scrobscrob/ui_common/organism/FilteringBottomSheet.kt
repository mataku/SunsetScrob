package com.mataku.scrobscrob.ui_common.organism

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.TimeRangeFiltering
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview

@Composable
fun FilteringBottomSheet(
  selectedTimeRangeFiltering: TimeRangeFiltering,
  onClick: (TimeRangeFiltering) -> Unit,
  modifier: Modifier
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(
        horizontal = 16.dp,
        vertical = 24.dp
      )
  ) {
    Text(
      text = "Select time-range",
      style = SunsetTextStyle.h7
    )

    Spacer(modifier = Modifier.height(16.dp))
    TimeRangeFiltering.values().forEach {
      TimeRangeCell(
        timeRangeValue = it.uiLabel,
        selected = it == selectedTimeRangeFiltering,
        onClick = {
          onClick.invoke(it)
        }
      )
    }

  }
}

@Composable
private fun TimeRangeCell(
  timeRangeValue: String,
  selected: Boolean,
  onClick: () -> Unit
) {
  Row(modifier = Modifier
    .fillMaxWidth()
    .height(48.dp)
    .clickable {
      onClick.invoke()
    }
    .padding(vertical = 12.dp)
  ) {
    Text(
      text = timeRangeValue,
      style = SunsetTextStyle.body1,
      modifier = Modifier.weight(1F)
    )

    if (selected) {
      Image(
        imageVector = Icons.Outlined.Check,
        contentDescription = "selected time-range",
        colorFilter = ColorFilter.tint(
          color = MaterialTheme.colors.onSurface
        )
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun FilteringBottomSheetPreview() {
  SunsetThemePreview {
    Surface {
      FilteringBottomSheet(
        selectedTimeRangeFiltering = TimeRangeFiltering.OVERALL,
        onClick = {},
        modifier = Modifier
      )
    }
  }
}
