package com.mataku.scrobscrob.account.ui.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mataku.scrobscrob.account.ui.viewmodel.LicenseViewModel
import com.mataku.scrobscrob.core.entity.LicenseArtifact
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.organism.ContentHeader
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.backgroundColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LicenseScreen(
  viewModel: LicenseViewModel = hiltViewModel(),
) {
  val systemUiController = rememberSystemUiController()
  systemUiController.setSystemBarsColor(
    color = LocalAppTheme.current.backgroundColor()
  )

  val uiState by viewModel.uiState.collectAsState()

  val context = LocalContext.current
  LazyColumn(
    content = {
      stickyHeader {
        ContentHeader(text = "Licenses")
      }
      items(uiState.licenseList) {
        LicenseCell(
          licenseArtifact = it,
          onLicenseArtifactTap = { url ->
            if (url.isNotEmpty()) {
              val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
              runCatching {
                context.startActivity(intent)
              }
            }
          }
        )
        HorizontalDivider()
      }
    },
    modifier = Modifier
      .fillMaxSize()
      .padding(
        horizontal = 16.dp
      )
  )
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
        vertical = 12.dp
      ),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    Text(
      text = licenseArtifact.name,
      style = SunsetTextStyle.body
    )
    Text(
      text = licenseArtifact.groupId,
      style = SunsetTextStyle.caption
    )

    FlowRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      licenseArtifact.spdxLicenses.forEach {
        Text(
          text = it.name,
          modifier = Modifier
            .clickable {
              onLicenseArtifactTap.invoke(it.url)
            },
          style = SunsetTextStyle.label
        )
      }
    }
  }
}
