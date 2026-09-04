package com.mataku.scrobscrob.account.ui.viewmodel

import com.mataku.scrobscrob.core.entity.LicenseArtifact
import com.mataku.scrobscrob.data.repository.LicenseRepository
import com.mataku.scrobscrob.test_helper.unit.CoroutinesListener
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class LicenseViewModelSpec : DescribeSpec({
  extension(CoroutinesListener())

  describe("init") {
    it("collects the license list into UI state") {
      val licenseRepository = mockk<LicenseRepository>()
      val artifacts = listOf(
        LicenseArtifact(
          artifactId = "alpha",
          groupId = "com.example",
          name = "Alpha",
          spdxLicenses = persistentListOf(),
          scm = null,
          version = "1.0.0",
        ),
      )
      coEvery { licenseRepository.licenseList() } returns flowOf(artifacts)

      val viewModel = LicenseViewModel(licenseRepository)
      viewModel.uiState.value.licenseList.size shouldBe 1
      viewModel.uiState.value.licenseList[0].name shouldBe "Alpha"
    }

    it("keeps the empty initial list when the flow throws") {
      val licenseRepository = mockk<LicenseRepository>()
      coEvery { licenseRepository.licenseList() } returns flow { throw RuntimeException("boom") }

      val viewModel = LicenseViewModel(licenseRepository)
      viewModel.uiState.value.licenseList.size shouldBe 0
    }
  }
})
