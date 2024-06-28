package com.mataku.scrobscrob.discover.ui.molecule

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.Tag
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.molecule.TopTags
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview

@Composable
internal fun ChartTagList(
  tagList: List<Tag>,
  onTagClick: (Tag) -> Unit
) {
  Column(
    modifier = Modifier.fillMaxWidth()
  ) {
    Text(
      text = "Hot Tags",
      style = SunsetTextStyle.headline,
      maxLines = 1,
      modifier = Modifier
        .padding(
          horizontal = 16.dp,
          vertical = 8.dp
        )
    )

    TopTags(
      tagList = tagList,
      onTagClick = onTagClick,
    )
  }
}

@Preview
@Composable
private fun ChartTagListPreview() {
  SunsetThemePreview {
    Surface {
      ChartTagList(
        tagList = listOf(
          Tag("rock", ""),
          Tag("pop", ""),
          Tag("jazz", ""),
          Tag("hip-hop", ""),
          Tag("electronic", ""),
          Tag("indie", ""),
          Tag("folk", ""),
          Tag("metal", ""),
          Tag("punk", ""),
          Tag("classical", "")
        ),
        onTagClick = {}
      )
    }
  }
}
