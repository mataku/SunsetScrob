package com.mataku.scrobscrob.ui_common.molecule

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.mataku.scrobscrob.core.entity.Tag
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview

@Composable
fun TopTags(
  tagList: List<Tag>,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .horizontalScroll(rememberScrollState()),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    Spacer(modifier = Modifier.width(8.dp))
    tagList.map {
      Tag(name = it.name)
    }
    Spacer(modifier = Modifier.width(8.dp))
  }
}

@Composable
private fun Tag(name: String) {
  SuggestionChip(
    onClick = {},
    label = {
      Text(
        text = name,
        style = SunsetTextStyle.label,
        modifier = Modifier.padding(8.dp)
      )
    },
    shape = ShapeDefaults.Large
  )
}

@Preview
@Composable
@ShowkaseComposable(name = "TagList", group = "TagList")
fun TopTagsPreview() {
  SunsetThemePreview {
    Surface {
      val tagList = listOf("Dance", "Rock", "Jazz").map {
        Tag(
          name = it,
          url = ""
        )
      }
      TopTags(tagList = tagList)
    }
  }
}
