package com.mataku.scrobscrob.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue

class SunsetIssueRegistry : IssueRegistry() {
  override val issues: List<Issue> = listOf(
    PreviewComposableVisibilityDetector.ISSUE,
    RepositoryReturnsFlowDetector.ISSUE,
    UiStateMustBeStateFlowDetector.ISSUE,
    UiStateMustBeImmutableDetector.ISSUE,
  )

  override val api: Int = CURRENT_API

  override val minApi: Int = 14

  override val vendor: Vendor = Vendor(
    vendorName = "sunsetscrob",
    identifier = "com.mataku.scrobscrob",
    feedbackUrl = "https://github.com/mataku/sunsetscrob/issues",
  )
}
