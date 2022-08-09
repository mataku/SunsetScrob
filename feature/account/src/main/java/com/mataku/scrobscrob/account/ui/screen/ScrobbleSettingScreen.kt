package com.mataku.scrobscrob.account.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mataku.scrobscrob.account.R
import com.mataku.scrobscrob.account.ui.viewmodel.ScrobbleSettingViewModel
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.organism.ContentHeader
import com.mataku.scrobscrob.ui_common.style.LocalScaffoldState
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScrobbleSettingScreen(
  viewModel: ScrobbleSettingViewModel = hiltViewModel()
) {
  val uiState = viewModel.uiState
  val scaffoldState = LocalScaffoldState.current
  val context = LocalContext.current
  uiState.event?.let {
    when (it) {
      is ScrobbleSettingViewModel.UiEvent.AllowAppError -> {
        LaunchedEffect(Unit) {
          scaffoldState.snackbarHostState.showSnackbar(
            context.getString(R.string.error_allow_scrobble_app)
          )
        }
      }
      else -> Unit
    }
    viewModel.popEvent()
  }
  val appleMusicAllowed = uiState.allowedApps.contains(APPLE_MUSIC_NAME)
  val spotifyAllowed = uiState.allowedApps.contains(SPOTIFY_NAME)
  LazyColumn(content = {
    stickyHeader {
      ContentHeader(text = stringResource(id = R.string.label_scrobble_setting))
    }

    item {
      ScrobbleSettingCell(title = APPLE_MUSIC_NAME, enabled = appleMusicAllowed, onTapCell = {
        viewModel.allowApp(APPLE_MUSIC_NAME)
      })

      ScrobbleSettingCell(title = SPOTIFY_NAME, enabled = spotifyAllowed, onTapCell = {
        viewModel.allowApp(SPOTIFY_NAME)
      })
    }
  }, modifier = Modifier.fillMaxSize())
}

@Composable
private fun ScrobbleSettingCell(
  title: String,
  enabled: Boolean,
  onTapCell: (String) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .height(64.dp)
      .padding(horizontal = 16.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = title,
      style = SunsetTextStyle.subtitle1,
      modifier = Modifier.weight(1F)
    )

    Switch(checked = enabled, onCheckedChange = { onTapCell.invoke(title) })
  }
}

private const val APPLE_MUSIC_NAME = "Apple Music"
private const val SPOTIFY_NAME = "Spotify"

@Preview(showBackground = true)
@Composable
private fun ScrobbleSettingCellPreview() {
  SunsetThemePreview {
    androidx.compose.material.Surface {
      ScrobbleSettingCell("Apple Music", true, {})
    }
  }
}
