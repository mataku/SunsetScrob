package com.mataku.scrobscrob.auth.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mataku.scrobscrob.auth.R
import com.mataku.scrobscrob.auth.ui.state.LoginScreenState
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.style.Colors
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.LocalScaffoldState
import com.mataku.scrobscrob.ui_common.style.SunsetTheme
import com.mataku.scrobscrob.ui_common.style.backgroundColor
import com.mataku.scrobscrob.ui_common.style.colors
import kotlinx.coroutines.launch
import com.mataku.scrobscrob.ui_common.R as uiCommonR

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LoginScreen(
  stateHolder: LoginScreenState
) {
  val scaffoldState = LocalScaffoldState.current
  val coroutineScope = rememberCoroutineScope()
  val uiState = stateHolder.uiState
  val currentTheme = LocalAppTheme.current
  val systemUiController = rememberSystemUiController()
  systemUiController.setNavigationBarColor(
    color = if (currentTheme == AppTheme.SUNSET) {
      Colors.SunsetBlue
    } else {
      currentTheme.backgroundColor()
    }
  )
  val navigationBarColor = MaterialTheme.colors.primary
  val systemBarColor = LocalAppTheme.current.backgroundColor()
  val context = LocalContext.current
  uiState.event?.let {
    when (it) {
      is LoginScreenState.UiEvent.LoginSuccess -> {
        systemUiController.setSystemBarsColor(color = systemBarColor)
        systemUiController.setNavigationBarColor(navigationBarColor)
        stateHolder.navigateToTop()
      }
      is LoginScreenState.UiEvent.LoginFailed -> {
        coroutineScope.launch {
          scaffoldState.snackbarHostState.showSnackbar(context.getString(R.string.error_login_failed))
        }
      }
      is LoginScreenState.UiEvent.EmptyPasswordError -> {
        coroutineScope.launch {
          scaffoldState.snackbarHostState.showSnackbar(context.getString(R.string.error_password_required))
        }
      }
      is LoginScreenState.UiEvent.EmptyUsernameError -> {
        coroutineScope.launch {
          scaffoldState.snackbarHostState.showSnackbar(context.getString(R.string.error_username_required))
        }
      }

    }
    stateHolder.popEvent()
  }
  Scaffold(scaffoldState = scaffoldState) {
    LoginContent(
      isLoading = uiState.isLoading,
      onLoginButtonTap = { id, password ->
        stateHolder.login(id, password)
      },
      onPrivacyPolicyTap = {
        stateHolder.navigateToPrivacyPolicy()
      },
      username = uiState.username,
      password = uiState.password,
      onUsernameUpdate = {
        stateHolder.onUsernameUpdate(it)
      },
      onPasswordUpdate = {
        stateHolder.onPasswordUpdate(it)
      }
    )
  }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun LoginContent(
  isLoading: Boolean,
  onLoginButtonTap: (String, String) -> Unit,
  onPrivacyPolicyTap: () -> Unit,
  username: String,
  password: String,
  onUsernameUpdate: (String) -> Unit,
  onPasswordUpdate: (String) -> Unit
) {
  var passwordVisible by remember {
    mutableStateOf(false)
  }
  val focusManager = LocalFocusManager.current
  val autofill = LocalAutofill.current
  val systemUiController = rememberSystemUiController()
  val navigationBackgroundColor = LocalAppTheme.current.colors().primary
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
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Spacer(modifier = Modifier.height(24.dp))

    Text(
      text = stringResource(id = uiCommonR.string.login_to_last_fm),
      fontSize = 20.sp,
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
      label = { Text(text = "Username") },
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
      label = { Text(text = "Password") },
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
      contentPadding = PaddingValues(vertical = 12.dp)
    ) {
      if (isLoading) {
        CircularProgressIndicator(
          modifier = Modifier
            .size(16.dp)
            .background(color = Color.Transparent)
            .align(alignment = Alignment.CenterVertically)
        )
      } else {
        Text(text = "Let me in!")
      }
    }

    Spacer(modifier = Modifier.height(32.dp))

    TextButton(onClick = {
      onPrivacyPolicyTap.invoke()
    }) {
      Text(text = "Privacy policy", style = SunsetTextStyle.button)
    }
  }

  DisposableEffect(Unit) {
    onDispose {
      systemUiController.setNavigationBarColor(navigationBackgroundColor)
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
