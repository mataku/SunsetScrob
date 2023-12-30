package com.mataku.scrobscrob.ui_common.organism

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.mataku.scrobscrob.core.entity.TimeRangeFiltering
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview

@Composable
fun FilteringBottomSheet(
  selectedTimeRangeFiltering: TimeRangeFiltering,
  onClick: (TimeRangeFiltering) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(
        vertical = 24.dp
      )
  ) {
    Text(
      text = "Select time-range",
      style = SunsetTextStyle.h7,
      modifier = Modifier
        .padding(
          horizontal = 16.dp,
        )
    )

    Spacer(modifier = Modifier.height(16.dp))
    TimeRangeFiltering.entries.forEach {
      TimeRangeCell(
        timeRangeValue = it.uiLabel,
        selected = it == selectedTimeRangeFiltering,
        onClick = {
          onClick.invoke(it)
        },
        modifier = Modifier
          .padding(
            horizontal = 16.dp,
          )
      )
    }

  }
}

@Composable
private fun TimeRangeCell(
  timeRangeValue: String,
  selected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier
) {
  Row(modifier = Modifier
    .fillMaxWidth()
    .height(48.dp)
    .clickable {
      onClick.invoke()
    }
    .padding(vertical = 12.dp)
    .then(modifier)
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
          color = MaterialTheme.colorScheme.onSurface
        )
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
@ShowkaseComposable(name = "FilteringBottomSheet", group = "BottomSheet")
fun FilteringBottomSheetPreview() {
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
