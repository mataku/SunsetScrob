package com.mataku.scrobscrob.auth.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mataku.scrobscrob.auth.R
import com.mataku.scrobscrob.auth.ui.viewmodel.LoginViewModel
import com.mataku.scrobscrob.auth.webauth.LastFmWebAuthLauncher
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetButton
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetCircularProgressIndicator
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetText
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetTextButton
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.LocalSnackbarHostState
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import com.mataku.scrobscrob.ui_common.style.onSurfaceColor
import kotlinx.coroutines.launch
import com.mataku.scrobscrob.ui_common.R as uiCommonR

@Composable
internal fun LoginScreen(
  viewModel: LoginViewModel,
  webAuthLauncher: LastFmWebAuthLauncher,
  navigateToHomeFromAuth: () -> Unit,
  navigateToPrivacyPolicy: () -> Unit,
  modifier: Modifier = Modifier
) {
  val coroutineScope = rememberCoroutineScope()
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val snackbarHostState = LocalSnackbarHostState.current
  val resources = LocalResources.current
  val launchWebAuth = webAuthLauncher.rememberLaunch(onResult = viewModel::onWebAuthResult)
  LaunchedEffect(uiState.events) {
    uiState.events.firstOrNull()?.let {
      when (it) {
        is LoginViewModel.UiEvent.LoginSuccess -> {
          navigateToHomeFromAuth.invoke()
        }

        is LoginViewModel.UiEvent.LoginFailed -> {
          coroutineScope.launch {
            snackbarHostState.showSnackbar(resources.getString(R.string.error_login_failed))
          }
        }
      }
      viewModel.popEvent(it)
    }
  }
  Box(modifier = modifier.fillMaxSize()) {
    LoginContent(
      isLoading = uiState.isLoading,
      onSignInTap = {
        uiState.webAuthUrl?.let { url ->
          viewModel.onWebAuthOpened()
          launchWebAuth(url)
        }
      },
      onPrivacyPolicyTap = navigateToPrivacyPolicy,
      modifier = Modifier.padding(top = 24.dp)
    )
    WebAuthScrim(visible = uiState.isWebAuthOpen)
  }
}

@Composable
private fun LoginContent(
  isLoading: Boolean,
  onSignInTap: () -> Unit,
  onPrivacyPolicyTap: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState()),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Column(
      modifier = Modifier
        .widthIn(max = 480.dp)
        .fillMaxWidth()
        .padding(horizontal = 40.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Spacer(modifier = Modifier.height(24.dp))

      SunsetText.Title(
        text = stringResource(id = uiCommonR.string.login_to_last_fm),
        fontWeight = FontWeight.Bold,
        modifier = Modifier.align(Alignment.CenterHorizontally),
        color = LocalAppTheme.current.onSurfaceColor(),
      )

      Spacer(modifier = Modifier.height(16.dp))

      SunsetText.Label(
        text = stringResource(id = R.string.web_auth_description),
        color = LocalAppTheme.current.onSurfaceColor(),
        textAlign = TextAlign.Center,
      )

      Spacer(modifier = Modifier.height(48.dp))

      SunsetButton(
        onClick = onSignInTap,
        modifier = Modifier
          .fillMaxWidth(),
        enabled = !isLoading,
        contentPadding = PaddingValues(vertical = 16.dp)
      ) {
        if (isLoading) {
          SunsetCircularProgressIndicator(
            modifier = Modifier
              .size(16.dp)
              .background(color = Color.Transparent)
              .align(alignment = Alignment.CenterVertically)
          )
        } else {
          SunsetText.Body(
            text = "Sign in with Last.fm",
            fontWeight = FontWeight.Medium,
          )
        }
      }

      Spacer(modifier = Modifier.height(32.dp))

      SunsetTextButton.Label(
        text = "Privacy policy",
        onClick = onPrivacyPolicyTap,
        color = LocalAppTheme.current.onSurfaceColor(),
      )
    }
  }
}

@Composable
private fun WebAuthScrim(
  visible: Boolean,
  modifier: Modifier = Modifier
) {
  AnimatedVisibility(
    visible = visible,
    enter = fadeIn(),
    exit = fadeOut(),
    modifier = modifier
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(color = Color.Black.copy(alpha = SCRIM_ALPHA))
    )
  }
}

private const val SCRIM_ALPHA = 0.32f

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
  SunsetThemePreview {
    LoginContent(
      isLoading = false,
      onSignInTap = {},
      onPrivacyPolicyTap = {},
    )
  }
}
