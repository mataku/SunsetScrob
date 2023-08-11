package com.mataku.scrobscrob.account

import android.content.Context

interface AppInfoProvider {
  fun appVersion(): String

  fun navigateToUiCatalogIntent(context: Context)
}
