package com.mataku.scrobscrob.account.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mataku.scrobscrob.account.ui.viewmodel.ThemeSelectorViewModel
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ThemeSelectorScreen(
  viewModel: ThemeSelectorViewModel,
  onBackPressed: () -> Unit,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val currentTheme = LocalAppTheme.current

  // Because there were cases where values flowed in unintentionally, control
  // TODO
  uiState.event?.let {
    when (it) {
      is ThemeSelectorViewModel.UiEvent.ThemeChanged -> {

      }
    }
    viewModel.popEvent()
  }

  uiState.theme?.let {
    LazyColumn(
      content = {
        items(AppTheme.entries.sortedBy {
          it.priority
        }) {
          ThemeCell(
            theme = it,
            selected = it == currentTheme,
            onTapTheme = { currentTheme ->
              viewModel.changeTheme(currentTheme)
            })
        }
      },
      modifier = modifier
        .fillMaxSize()
    )
  }
  BackHandler() {
    onBackPressed.invoke()
  }
}

@Composable
private fun ThemeCell(
  theme: AppTheme,
  selected: Boolean,
  onTapTheme: (AppTheme) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .height(48.dp)
      .clickable {
        onTapTheme.invoke(theme)
      }
      .padding(horizontal = 16.dp, vertical = 12.dp)
  ) {
    Text(
      text = theme.displayName,
      style = SunsetTextStyle.body,
      modifier = Modifier.weight(1F)
    )

    if (selected) {
      Image(
        imageVector = Icons.Outlined.Check,
        contentDescription = "selected theme",
        colorFilter = ColorFilter.tint(
          color = MaterialTheme.colorScheme.onSurface
        )
      )
    }
  }
}
