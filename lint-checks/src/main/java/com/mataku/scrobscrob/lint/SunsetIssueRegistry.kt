package com.mataku.scrobscrob.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue

class SunsetIssueRegistry : IssueRegistry() {
  override val issues: List<Issue> = listOf(
    PreferLocalAppThemeColorDetector.ISSUE,
    PreferSunsetAlertDialogDetector.ISSUE,
    PreferSunsetBottomSheetDetector.ISSUE,
    PreferSunsetButtonDetector.ISSUE,
    PreferSunsetChipDetector.ISSUE,
    PreferSunsetCircularProgressIndicatorDetector.ISSUE,
    PreferSunsetFloatingActionButtonDetector.ISSUE,
    PreferSunsetHorizontalDividerDetector.ISSUE,
    PreferSunsetIconButtonDetector.ISSUE,
    PreferSunsetIconDetector.ISSUE,
    PreferSunsetIconToggleButtonDetector.ISSUE,
    PreferSunsetModalBottomSheetDetector.ISSUE,
    PreferSunsetPullToRefreshBoxDetector.ISSUE,
    PreferSunsetScaffoldDetector.ISSUE,
    PreferSunsetSnackbarHostDetector.ISSUE,
    PreferSunsetSurfaceDetector.ISSUE,
    PreferSunsetSwitchDetector.ISSUE,
    PreferSunsetTabRowDetector.ISSUE,
    PreferSunsetTextButtonDetector.ISSUE,
    PreferSunsetTextDetector.ISSUE,
    PreferSunsetTextFieldDetector.ISSUE,
    PreferSunsetTopAppBarDetector.ISSUE,
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
