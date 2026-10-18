package com.mataku.scrobscrob.account.ui.screen

import com.mataku.scrobscrob.account.ui.viewmodel.LicenseViewModel
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.core.entity.LicenseArtifact
import com.mataku.scrobscrob.core.entity.SpdxLicense
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import io.mockk.every
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("VRT")
class LicenseScreenTest {
  private val apache = SpdxLicense(
    identifier = "Apache-2.0",
    name = "Apache License 2.0",
    url = "https://www.apache.org/licenses/LICENSE-2.0",
  )

  private val viewModel: LicenseViewModel = mockk {
    every { uiState } returns MutableStateFlow(
      LicenseViewModel.LicenseUiState(
        licenseList = persistentListOf(
          LicenseArtifact(
            artifactId = "ktor-client-core",
            groupId = "io.ktor",
            name = "Ktor",
            scm = null,
            spdxLicenses = persistentListOf(apache),
            version = "3.6.0",
          ),
          LicenseArtifact(
            artifactId = "kotlinx-coroutines-core",
            groupId = "org.jetbrains.kotlinx",
            name = "kotlinx-coroutines-core",
            scm = null,
            spdxLicenses = persistentListOf(
              apache,
              SpdxLicense(
                identifier = "MIT",
                name = "MIT License",
                url = "https://opensource.org/license/mit",
              ),
            ),
            version = "1.11.0",
          ),
        ),
      )
    )
  }

  @Test
  fun layout() {
    captureScreenshot(
      appTheme = AppTheme.DARK,
      content = {
        LicenseScreen(
          viewModel = viewModel,
          onBackPressed = {},
        )
      },
      fileName = "license_screen.png"
    )
  }

  @Test
  fun layout_light() {
    captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = {
        LicenseScreen(
          viewModel = viewModel,
          onBackPressed = {},
        )
      },
      fileName = "license_screen_light.png"
    )
  }
}
