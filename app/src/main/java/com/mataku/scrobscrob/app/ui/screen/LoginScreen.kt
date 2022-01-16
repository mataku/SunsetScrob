package com.mataku.scrobscrob.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.app.ui.viewmodel.LoginViewModel
import com.mataku.scrobscrob.ui_common.style.Colors
import com.mataku.scrobscrob.ui_common.style.SunsetTheme

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel
) {
    val uiState = viewModel.uiState
    LoginContent(
        isLoading = uiState.isLoading,
        onLoginButtonTap = { id, password ->
            viewModel.authorize(id, password)
        }
    )
}

@Composable
fun LoginContent(
    isLoading: Boolean,
    onLoginButtonTap: (String, String) -> Unit
) {
    var idText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.ContentBackground)
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .weight(1F),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = stringResource(id = R.string.login_to_last_fm),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = idText,
                onValueChange = {
                    idText = it
                },
                label = { Text(text = "Username or Email") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = passwordText,
                onValueChange = {
                    passwordText = it
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                label = { Text(text = "Password") }
            )
        }
        Column() {
            Button(
                onClick = {
                    onLoginButtonTap(idText, passwordText)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                Text(text = "Login")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    SunsetTheme {
        Surface {
            LoginScreen(
                navController = NavController(LocalContext.current),
                viewModel = viewModel()
            )
        }
    }
}