package com.mataku.scrobscrob.account.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mataku.scrobscrob.account.R
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.ui_common.organism.ContentHeader
import com.mataku.scrobscrob.ui_common.style.Colors
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.backgroundColor
import com.mikepenz.aboutlibraries.ui.compose.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.LibraryDefaults

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LicenseScreen() {
  val systemUiController = rememberSystemUiController()
  systemUiController.setNavigationBarColor(
    color = if (LocalAppTheme.current == AppTheme.SUNSET) {
      Colors.SunsetBlue
    } else {
      LocalAppTheme.current.backgroundColor()
    }
  )
  LibrariesContainer(
    modifier = Modifier.fillMaxSize(),
    header = {
      stickyHeader {
        ContentHeader(text = stringResource(id = R.string.item_license))
      }
    },
    colors = LibraryDefaults.libraryColors(
      backgroundColor = MaterialTheme.colorScheme.background,
      contentColor = MaterialTheme.colorScheme.onSurface
    )
  )
}
