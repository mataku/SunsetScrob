package com.mataku.scrobscrob.auth.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mataku.scrobscrob.auth.R
import com.mataku.scrobscrob.auth.ui.viewmodel.LoginViewModel
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetButton
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetCircularProgressIndicator
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetIcon
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetIconButton
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetText
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetTextButton
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetTextField
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.LocalSnackbarHostState
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import com.mataku.scrobscrob.ui_common.style.onSurfaceColor
import kotlinx.coroutines.launch
import com.mataku.scrobscrob.ui_common.R as uiCommonR

@Composable
internal fun LoginScreen(
  viewModel: LoginViewModel,
  navigateToHomeFromAuth: () -> Unit,
  navigateToPrivacyPolicy: () -> Unit,
  modifier: Modifier = Modifier
) {
  val coroutineScope = rememberCoroutineScope()
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val context = LocalContext.current
  val snackbarHostState = LocalSnackbarHostState.current
  val resources = LocalResources.current
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

        is LoginViewModel.UiEvent.EmptyPasswordError -> {
          coroutineScope.launch {
            snackbarHostState.showSnackbar(resources.getString(R.string.error_password_required))
          }
        }

        is LoginViewModel.UiEvent.EmptyUsernameError -> {
          coroutineScope.launch {
            snackbarHostState.showSnackbar(resources.getString(R.string.error_username_required))
          }
        }
      }
      viewModel.popEvent(it)
    }
  }
  LoginContent(
    isLoading = uiState.isLoading,
    onLoginButtonTap = { id, password ->
      viewModel.authorize(id, password)
    },
    onPrivacyPolicyTap = navigateToPrivacyPolicy,
    username = uiState.username,
    password = uiState.password,
    onUsernameUpdate = viewModel::updateUsername,
    onPasswordUpdate = viewModel::updatePassword,
    modifier = modifier
  )
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalLayoutApi::class)
@Composable
private fun LoginContent(
  isLoading: Boolean,
  onLoginButtonTap: (String, String) -> Unit,
  onPrivacyPolicyTap: () -> Unit,
  username: String,
  password: String,
  onUsernameUpdate: (String) -> Unit,
  onPasswordUpdate: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var passwordVisible by remember {
    mutableStateOf(false)
  }
  val focusManager = LocalFocusManager.current
  // Stored data with "remember { mutableStateOf("") }" will blow up the data in AutoFill#onFill,
  //  so manages input data in ViewModel (TODO: details)
  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .imePadding()
      .imeNestedScroll(),
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

      Spacer(modifier = Modifier.height(24.dp))

      SunsetTextField(
        value = username,
        onValueChange = {
          onUsernameUpdate.invoke(it)
        },
        keyboardOptions = KeyboardOptions(
          keyboardType = KeyboardType.Email,
          imeAction = ImeAction.Next
        ),
        singleLine = true,
        label = {
          SunsetText.Label(
            text = "Username",
            color = LocalAppTheme.current.onSurfaceColor(),
          )
        },
        modifier = Modifier
          .fillMaxWidth()
          .semantics {
            contentType = ContentType.Username
          }
      )

      Spacer(modifier = Modifier.height(16.dp))

      SunsetTextField(
        value = password,
        onValueChange = {
          onPasswordUpdate.invoke(it)
        },
        keyboardOptions = KeyboardOptions(
          keyboardType = KeyboardType.Password,
          imeAction = ImeAction.Done
        ),
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
          val icon = if (passwordVisible) {
            Icons.Filled.Visibility
          } else {
            Icons.Filled.VisibilityOff
          }
          SunsetIconButton(onClick = {
            passwordVisible = !passwordVisible
          }) {
            SunsetIcon(imageVector = icon, "password visibility toggle")
          }
        },
        keyboardActions = KeyboardActions(
          onDone = {
            focusManager.clearFocus()
          }
        ),
        label = {
          SunsetText.Label(
            text = "Password",
            color = LocalAppTheme.current.onSurfaceColor(),
          )
        },
        modifier = Modifier
          .fillMaxWidth()
          .semantics {
            contentType = ContentType.Password
          }
      )

      Spacer(modifier = Modifier.height(48.dp))

      SunsetButton(
        onClick = {
          focusManager.clearFocus()
          onLoginButtonTap(username, password)
        },
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
            text = "Let me in!",
            fontWeight = FontWeight.Medium,
          )
        }
      }

      Spacer(modifier = Modifier.height(32.dp))

      SunsetTextButton.Label(
        text = "Privacy policy",
        onClick = onPrivacyPolicyTap,
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
  SunsetThemePreview {
    LoginContent(
      isLoading = false,
      onLoginButtonTap = { _, _ -> },
      onPrivacyPolicyTap = {},
      username = "",
      password = "",
      onUsernameUpdate = {},
      onPasswordUpdate = {}
    )
  }
}
