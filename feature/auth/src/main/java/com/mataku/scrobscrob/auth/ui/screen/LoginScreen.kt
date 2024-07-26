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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mataku.scrobscrob.auth.R
import com.mataku.scrobscrob.auth.ui.viewmodel.LoginViewModel
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.style.LocalSnackbarHostState
import com.mataku.scrobscrob.ui_common.style.SunsetTheme
import kotlinx.coroutines.launch
import com.mataku.scrobscrob.ui_common.R as uiCommonR

@Composable
fun LoginScreen(
  viewModel: LoginViewModel,
  navigateToHomeFromAuth: () -> Unit,
  navigateToPrivacyPolicy: () -> Unit,
  modifier: Modifier = Modifier
) {
  val coroutineScope = rememberCoroutineScope()
  val uiState by viewModel.uiState.collectAsState()
  val context = LocalContext.current
  val snackbarHostState = LocalSnackbarHostState.current
  uiState.event?.let {
    when (it) {
      is LoginViewModel.UiEvent.LoginSuccess -> {
        navigateToHomeFromAuth.invoke()
      }

      is LoginViewModel.UiEvent.LoginFailed -> {
        coroutineScope.launch {
          snackbarHostState.showSnackbar(context.getString(R.string.error_login_failed))
        }
      }

      is LoginViewModel.UiEvent.EmptyPasswordError -> {
        coroutineScope.launch {
          snackbarHostState.showSnackbar(context.getString(R.string.error_password_required))
        }
      }

      is LoginViewModel.UiEvent.EmptyUsernameError -> {
        coroutineScope.launch {
          snackbarHostState.showSnackbar(context.getString(R.string.error_username_required))
        }
      }
    }
    viewModel.popEvent()
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
  val autofill = LocalAutofill.current
  val usernameAutofillNode = AutofillNode(autofillTypes = listOf(AutofillType.Username), onFill = {
    onUsernameUpdate.invoke(it)
  })
  val passwordAutofillNode = AutofillNode(autofillTypes = listOf(AutofillType.Password), onFill = {
    onPasswordUpdate.invoke(it)
  })
  LocalAutofillTree.current += usernameAutofillNode
  LocalAutofillTree.current += passwordAutofillNode

  // Stored data with "remember { mutableStateOf("") }" will blow up the data in AutoFill#onFill,
  //  so manages input data in ViewModel (TODO: details)
  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 16.dp)
      .imePadding()
      .imeNestedScroll(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Spacer(modifier = Modifier.height(24.dp))

    Text(
      text = stringResource(id = uiCommonR.string.login_to_last_fm),
      fontSize = 20.sp,
      style = SunsetTextStyle.title,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.align(Alignment.CenterHorizontally)
    )

    Spacer(modifier = Modifier.height(24.dp))

    OutlinedTextField(
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
        Text(
          text = "Username",
          style = SunsetTextStyle.label
        )
      },
      modifier = Modifier
        .onGloballyPositioned {
          usernameAutofillNode.boundingBox = it.boundsInWindow()
        }
        .align(Alignment.CenterHorizontally)
        .fillMaxWidth()
        .padding(horizontal = 24.dp)
        .onFocusChanged {
          autofill?.run {
            if (it.isFocused) {
              requestAutofillForNode(usernameAutofillNode)
            } else {
              cancelAutofillForNode(usernameAutofillNode)
            }
          }
        }
    )

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
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
        IconButton(onClick = {
          passwordVisible = !passwordVisible
        }) {
          Icon(imageVector = icon, "password visibility toggle")
        }
      },
      keyboardActions = KeyboardActions(
        onDone = {
          focusManager.clearFocus()
        }
      ),
      label = {
        Text(
          text = "Password",
          style = SunsetTextStyle.label
        )
      },
      modifier = Modifier
        .onGloballyPositioned {
          passwordAutofillNode.boundingBox = it.boundsInWindow()
        }
        .align(Alignment.CenterHorizontally)
        .fillMaxWidth()
        .padding(horizontal = 24.dp)
        .onFocusChanged {
          autofill?.run {
            if (it.isFocused) {
              requestAutofillForNode(passwordAutofillNode)
            } else {
              cancelAutofillForNode(passwordAutofillNode)
            }
          }
        }
    )

    Spacer(modifier = Modifier.height(48.dp))

    Button(
      onClick = {
        focusManager.clearFocus()
        onLoginButtonTap(username, password)
      },
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp),
      enabled = !isLoading,
      contentPadding = PaddingValues(vertical = 16.dp)
    ) {
      if (isLoading) {
        CircularProgressIndicator(
          modifier = Modifier
            .size(16.dp)
            .background(color = Color.Transparent)
            .align(alignment = Alignment.CenterVertically)
        )
      } else {
        Text(
          text = "Let me in!",
          style = SunsetTextStyle.label
        )
      }
    }

    Spacer(modifier = Modifier.height(32.dp))

    TextButton(onClick = {
      onPrivacyPolicyTap.invoke()
    }) {
      Text(text = "Privacy policy", style = SunsetTextStyle.button)
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
  SunsetTheme {
    Surface {
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
}
