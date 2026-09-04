package com.mataku.scrobscrob.account.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mataku.scrobscrob.account.generated.resources.Res
import com.mataku.scrobscrob.account.generated.resources.item_license
import com.mataku.scrobscrob.account.ui.viewmodel.LicenseViewModel
import com.mataku.scrobscrob.core.entity.LicenseArtifact
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetHorizontalDivider
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetIcon
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetIconButton
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetScaffold
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetText
import com.mataku.scrobscrob.ui_common.component.designsystem.SunsetTopAppBar
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun LicenseScreen(
  viewModel: LicenseViewModel,
  onBackPressed: () -> Unit,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  val uriHandler = LocalUriHandler.current
  SunsetScaffold(
    modifier = modifier,
    topBar = {
      SunsetTopAppBar(
        title = {
          SunsetText.Title(text = stringResource(Res.string.item_license))
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
        items(uiState.licenseList) {
          LicenseCell(
            licenseArtifact = it,
            onLicenseArtifactTap = { url ->
              if (url.isNotEmpty()) {
                runCatching {
                  uriHandler.openUri(url)
                }
              }
            }
          )
          SunsetHorizontalDivider()
        }
      },
      contentPadding = WindowInsets.navigationBars.asPaddingValues(),
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .padding(
          horizontal = 16.dp
        )
    )
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LicenseCell(
  licenseArtifact: LicenseArtifact,
  onLicenseArtifactTap: (String) -> Unit
) {
  Column(
    modifier = Modifier
      .padding(
        vertical = 12.dp,
      ),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    SunsetText.Body(
      text = licenseArtifact.name,
    )
    SunsetText.Caption(
      text = licenseArtifact.groupId,
    )

    FlowRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      licenseArtifact.spdxLicenses.forEach {
        SunsetText.Label(
          text = it.name,
          modifier = Modifier
            .clickable {
              onLicenseArtifactTap.invoke(it.url)
            },
        )
      }
    }
  }
}
