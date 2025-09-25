package com.mataku.scrobscrob.ui_common.molecule

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ChipColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
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
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun TopTags(
  tagList: ImmutableList<Tag>,
  modifier: Modifier = Modifier,
  onTagClick: (Tag) -> Unit = {},
) {
  Row(
    modifier = modifier
      .horizontalScroll(rememberScrollState()),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    Spacer(modifier = Modifier.width(8.dp))
    tagList.map {
      Tag(
        name = it.name,
        onTagClick = {
          onTagClick.invoke(it)
        }
      )
    }
    Spacer(modifier = Modifier.width(8.dp))
  }
}

@Composable
private fun Tag(
  name: String,
  onTagClick: () -> Unit = {}
) {
  SuggestionChip(
    onClick = {
      onTagClick.invoke()
    },
    label = {
      Text(
        text = name,
        style = SunsetTextStyle.label,
        modifier = Modifier.padding(8.dp)
      )
    },
    shape = ShapeDefaults.Large,
    border = SuggestionChipDefaults.suggestionChipBorder(
      enabled = true,
      borderColor = MaterialTheme.colorScheme.onSecondary,
    )
  )
}

@Preview
@Composable
private fun TopTagsPreview() {
  SunsetThemePreview {
    Surface {
      val tagList = listOf("Dance", "Rock", "Jazz").map {
        Tag(
          name = it,
          url = ""
        )
      }.toImmutableList()
      TopTags(tagList = tagList, onTagClick = {})
    }
  }
}
