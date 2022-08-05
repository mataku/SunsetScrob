package com.mataku.scrobscrob.ui_common.molecule

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.core.entity.Tag

@Composable
fun TopTags(
  tagList: List<Tag>
) {
  Row(
    modifier = Modifier
      .horizontalScroll(rememberScrollState())
      .padding(horizontal = 16.dp, vertical = 16.dp)
  ) {
    tagList.map {
      com.mataku.scrobscrob.ui_common.molecule.Tag(name = it.name)
      Spacer(modifier = Modifier.width(8.dp))
    }
  }
}

@Composable
private fun Tag(name: String) {
  Chip(name = name, toggleable = false)
}
