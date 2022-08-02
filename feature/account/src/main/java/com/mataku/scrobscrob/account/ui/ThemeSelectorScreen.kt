package com.mataku.scrobscrob.account.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mataku.scrobscrob.account.R
import com.mataku.scrobscrob.account.ui.state.ThemeSelectorState
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.organism.ContentHeader
import com.mataku.scrobscrob.ui_common.style.Colors
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.backgroundColor
import com.mataku.scrobscrob.ui_common.style.sunsetBackgroundGradient

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ThemeSelectorScreen(
  state: ThemeSelectorState
) {
  val uiState = state.uiState
  val systemUiController = rememberSystemUiController()
  val currentTheme = LocalAppTheme.current
  val initialState = remember {
    mutableStateOf(true)
  }

  if (initialState.value) {
    systemUiController.setNavigationBarColor(
      color = if (currentTheme == AppTheme.SUNSET) {
        Colors.SunsetBlue
      } else {
        currentTheme.backgroundColor()
      }
    )
    initialState.value = false
  }

  // Because there were cases where values flowed in unintentionally, control
  // TODO
  uiState.event?.let {
    when (it) {
      is ThemeSelectorState.UiEvent.ThemeChanged -> {
        systemUiController.setNavigationBarColor(
          color = if (it.theme == AppTheme.SUNSET) {
            Colors.SunsetBlue
          } else {
            it.theme.backgroundColor()
          }
        )
      }
    }
    state.popEvent()
  }

  uiState.theme?.let {
    LazyColumn(
      content = {
        stickyHeader {
          ContentHeader(text = stringResource(id = R.string.title_theme_selector))
        }
        items(AppTheme.values().sortedBy {
          it.priority
        }) {
          ThemeCell(
            theme = it,
            selected = it == currentTheme,
            onTapTheme = { currentTheme ->
              state.changeTheme(currentTheme)
            })
        }
      },
      modifier = if (LocalAppTheme.current == AppTheme.SUNSET) {
          Modifier
              .fillMaxSize()
              .background(
                  brush = sunsetBackgroundGradient
              )
      } else {
        Modifier
          .fillMaxSize()
      }
    )
  }
  val navigationBarColor = MaterialTheme.colors.primary
  BackHandler() {
    systemUiController.setNavigationBarColor(navigationBarColor)
    state.back()
  }
}

@Composable
private fun ThemeCell(
  theme: AppTheme,
  selected: Boolean,
  onTapTheme: (AppTheme) -> Unit
) {
  Row(modifier = Modifier
      .fillMaxWidth()
      .height(48.dp)
      .clickable {
          onTapTheme.invoke(theme)
      }
      .padding(horizontal = 16.dp, vertical = 12.dp)
  ) {
    Text(
      text = theme.displayName,
      style = SunsetTextStyle.body1,
      modifier = Modifier.weight(1F)
    )

    if (selected) {
      Image(
        imageVector = Icons.Outlined.Check,
        contentDescription = "selected theme",
        colorFilter = ColorFilter.tint(
          color = MaterialTheme.colors.onSurface
        )
      )
    }
  }
}
