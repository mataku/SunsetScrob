package com.mataku.scrobscrob.ui_common.navigation

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data object LoginKey : SunsetNavKey

@Immutable
@Serializable
data object PrivacyPolicyKey : SunsetNavKey

@Immutable
@Serializable
data class WebViewKey(val url: String) : SunsetNavKey
