package com.mataku.scrobscrob.account.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mataku.scrobscrob.account.generated.resources.Res
import com.mataku.scrobscrob.account.generated.resources.error_allow_scrobble_app
import com.mataku.scrobscrob.account.generated.resources.label_scrobble_setting
import com.mataku.scrobscrob.account.ui.viewmodel.ScrobbleSettingViewModel
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetIcon
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetIconButton
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetScaffold
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetSwitch
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetText
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetTopAppBar
import com.mataku.scrobscrob.ui_common.style.LocalSnackbarHostState
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ScrobbleSettingScreen(
  viewModel: ScrobbleSettingViewModel,
  onBackPressed: () -> Unit,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val snackbarHostState = LocalSnackbarHostState.current
  val allowAppErrorMessage = stringResource(Res.string.error_allow_scrobble_app)
  uiState.event?.let {
    when (it) {
      is ScrobbleSettingViewModel.UiEvent.AllowAppError -> {
        LaunchedEffect(Unit) {
          snackbarHostState.showSnackbar(allowAppErrorMessage)
        }
      }

      else -> Unit
    }
    viewModel.popEvent()
  }
  val appleMusicAllowed = uiState.allowedApps.contains(APPLE_MUSIC_NAME.mappedApp())
  val spotifyAllowed = uiState.allowedApps.contains(SPOTIFY_NAME.mappedApp())
  val youTubeMusicAllowed = uiState.allowedApps.contains(YOUTUBE_MUSIC_NAME.mappedApp())

  SunsetScaffold(
    modifier = modifier,
    topBar = {
      SunsetTopAppBar(
        title = {
          SunsetText.Title(text = stringResource(Res.string.label_scrobble_setting))
        },
        navigationIcon = {
          SunsetIconButton(onClick = onBackPressed) {
            SunsetIcon(
              imageVector = Icons.AutoMirrored.Default.ArrowBack,
              contentDescription = "Back"
            )
          }
        },
      )
    }
  ) { paddingValues ->
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
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    )
  }
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
    SunsetText.Subtitle(
      text = title,
      modifier = Modifier.weight(1F)
    )

    SunsetSwitch(checked = enabled, onCheckedChange = { onTapCell.invoke(title, it) })
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
    ScrobbleSettingCell("Apple Music", true) { _, _ -> }
  }
}
