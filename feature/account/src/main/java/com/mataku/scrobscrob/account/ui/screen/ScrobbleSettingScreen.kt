package com.mataku.scrobscrob.account.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.account.R
import com.mataku.scrobscrob.account.ui.viewmodel.ScrobbleSettingViewModel
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.style.LocalSnackbarHostState
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScrobbleSettingScreen(
  viewModel: ScrobbleSettingViewModel,
  modifier: Modifier = Modifier
) {
  val uiState = viewModel.uiState
  val snackbarHostState = LocalSnackbarHostState.current
  val context = LocalContext.current
  uiState.event?.let {
    when (it) {
      is ScrobbleSettingViewModel.UiEvent.AllowAppError -> {
        LaunchedEffect(Unit) {
          snackbarHostState.showSnackbar(
            context.getString(R.string.error_allow_scrobble_app)
          )
        }
      }

      else -> Unit
    }
    viewModel.popEvent()
  }
  val appleMusicAllowed = uiState.allowedApps.contains(APPLE_MUSIC_NAME.mappedApp())
  val spotifyAllowed = uiState.allowedApps.contains(SPOTIFY_NAME.mappedApp())
  val youTubeMusicAllowed = uiState.allowedApps.contains(YOUTUBE_MUSIC_NAME.mappedApp())

  LazyColumn(
    content = {
      item {
        ScrobbleSettingCell(
          title = APPLE_MUSIC_NAME,
          enabled = appleMusicAllowed,
          onTapCell = { appName, enable ->
            viewModel.changeAppScrobbleState(appName, enable)
          }
        )

        ScrobbleSettingCell(
          title = SPOTIFY_NAME,
          enabled = spotifyAllowed,
          onTapCell = { appName, enable ->
            viewModel.changeAppScrobbleState(appName, enable)
          }
        )

        ScrobbleSettingCell(
          title = YOUTUBE_MUSIC_NAME,
          enabled = youTubeMusicAllowed,
          onTapCell = { appName, enable ->
            viewModel.changeAppScrobbleState(appName, enable)
          }
        )

      }
    },
    modifier = modifier.fillMaxSize()
  )
}

@Composable
private fun ScrobbleSettingCell(
  title: String,
  enabled: Boolean,
  onTapCell: (String, Boolean) -> Unit
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
      style = SunsetTextStyle.subtitle,
      modifier = Modifier.weight(1F)
    )

    Switch(checked = enabled, onCheckedChange = { onTapCell.invoke(title, it) })
  }
}

internal fun String.mappedApp(): String? {
  return when (this) {
    APPLE_MUSIC_NAME -> {
      "com.apple.android.music"
    }

    SPOTIFY_NAME -> {
      "com.spotify.music"
    }

    YOUTUBE_MUSIC_NAME -> {
      "com.google.android.apps.youtube.music"
    }

    else -> null
  }
}

private const val APPLE_MUSIC_NAME = "Apple Music"
private const val SPOTIFY_NAME = "Spotify"
private const val YOUTUBE_MUSIC_NAME = "YouTube Music"

@Preview(showBackground = true)
@Composable
private fun ScrobbleSettingCellPreview() {
  SunsetThemePreview {
    Surface {
      ScrobbleSettingCell("Apple Music", true) { _, _ -> }
    }
  }
}
