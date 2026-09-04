package com.mataku.scrobscrob.account.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mataku.scrobscrob.account.generated.resources.Res
import com.mataku.scrobscrob.account.generated.resources.item_privacy_policy
import com.mataku.scrobscrob.ui_common.component.SunsetWebView
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetIcon
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetIconButton
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetScaffold
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetText
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetTopAppBar
import org.jetbrains.compose.resources.stringResource

private const val PRIVACY_POLICY_URL = "https://mataku.github.io/sunsetscrob/index.html"

@Composable
internal fun PrivacyPolicyScreen(
  onBackPressed: () -> Unit,
  modifier: Modifier = Modifier
) {
  SunsetScaffold(
    modifier = modifier,
    topBar = {
      SunsetTopAppBar(
        title = {
          SunsetText.Title(text = stringResource(Res.string.item_privacy_policy))
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
    SunsetWebView(
      url = PRIVACY_POLICY_URL,
      openLinksExternally = true,
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    )
  }
}
